package team.stephen.sunshine.controller.other;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.bean.Pagination;
import team.stephen.sunshine.model.other.bean.cssci.*;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.CssciService;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.service.other.parse.ParserFactory;
import team.stephen.sunshine.service.other.parse.ParserType;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleAuthorParser;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleCitationParser;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleDetailParser;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static team.stephen.sunshine.util.constant.CrawError.ERROR_DETAIL;
import static team.stephen.sunshine.util.constant.CrawError.ERROR_PAGE;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("other/cssci")
public class CssciController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CssciController.class);
    private static final Integer PAGE_SIZE = 50;
    @Autowired
    private CssciService cssciService;
    @Autowired
    private CrawlErrorService crawlErrorService;
    @Autowired
    private CssciCrawlResource resource;

    private Parser pageParser = ParserFactory.INSTANCE.getParser(ParserType.CSSCI_PAGE).get();
    private Parser artOvParser = ParserFactory.INSTANCE.getParser(ParserType.CSSCI_ARTICLE_OVERVIEW).get();

    private CssciArticleDetailParser detailParser = new CssciArticleDetailParser();
    private CssciArticleAuthorParser authorParser = new CssciArticleAuthorParser();
    private CssciArticleCitationParser citationParser = new CssciArticleCitationParser();
    private String dir = "C:\\USERS\\STEPHEN\\Desktop\\sunshine\\cssci\\";

    @ApiOperation(value = "查询CSSCI论文ID 从文件中", httpMethod = "GET", response = Response.class)
    @RequestMapping(value = "crawlArticleFromFile", method = RequestMethod.GET)
    public Response crawlArticleFromFile() {
        String filepath = dir + "journal-extra.txt";
        try {
            List<String> journals = Files.readLines(new File(filepath), Charsets.UTF_8);
            journals.forEach(this::handleLine);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.success(null);
    }

    private void handleLine(String line) {
        String[] tmp = line.split("\t");
        String journal = tmp[0];

        int startYear = getYear(tmp[1], true);
        int endYear = getYear(tmp[1], false);
        LOGGER.info(journal + "\t" + startYear + "\t" + endYear + "\n");
        crawlArticleBaseInfo(journal, startYear, endYear);

        String saveFile = dir + "crawled.txt";
        try {
            FileWriter writer = new FileWriter((new File(saveFile)), true);
            writer.append(journal + "\t" + startYear + "\t" + endYear + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getYear(String s, boolean startYear) {
        if (s.contains("-")) {
            if (startYear) {
                return Integer.parseInt(s.substring(0, s.indexOf("-")));
            } else {
                return Integer.parseInt(s.substring(s.indexOf("-") + 1));
            }
        }
        return Integer.parseInt(s);
    }

    @ApiOperation(value = "查询CSSCI论文ID", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qkname", value = "期刊名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "startYear", value = "开始时间", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "endYear", value = "结束时间", required = true, dataType = "int", paramType = "query")})

    @RequestMapping(value = "crawlArticleBaseInfo", method = RequestMethod.GET)
    public Response crawlArticleBaseInfo(String qkname, Integer startYear, Integer endYear) {

        if (StringUtils.isBlank(qkname) || startYear == null || endYear == null) {
            return Response.error(ResultEnum.NULL_PARAMETER);
        }
        LOGGER.info("start 2 crawl ,param: qkname is " + qkname + " \t startYear is : " + startYear + "\t endYear is " + endYear);

        CssciPaperParam param = new CssciPaperParam(resource);
        param.setTitle(qkname);
        param.setPagenow(1);
        param.setStartYear(String.valueOf(startYear));
        param.setEndYear(String.valueOf(endYear));
        param.setPageSize(50);
        LOGGER.info(param.getUrl());
        try {
            crawlPage(qkname, param);
        } catch (Exception e) {
            LOGGER.error("error:", e);
        }
        return Response.success(null);
    }

    private boolean crawlPage(String qkname, CssciPaperParam param) throws IOException, InterruptedException, CrawlException {
        Integer startYear = Integer.valueOf(param.getStartYear());
        Integer endYear = Integer.valueOf(param.getEndYear());
        HttpResponse response = HttpUtils.httpGet(param.getUrl(), normalHeaders(param));
        String html = IOUtils.toString(response.getEntity().getContent());
        Thread.sleep(new Random().nextInt(1) * 1000);

        Pagination pagination = (Pagination) pageParser.parse(html).get(0);
        if (pagination.getTotal() == null) {
            LOGGER.error("query pagination info error : " + param.getUrl());
            addError(param, ERROR_PAGE);
            return false;
        }
        if (Objects.equal(param.getStartYear(), param.getEndYear()) && pagination.getTotal() > 1000) {
            LOGGER.info("startYear equals endYear,size more than 1000. journal:" + param.getTitle() + "\t year: " + param.getStartYear() + "\t , ,size: " + pagination.getTotal());
            if (pagination.getTotal() <= 2000) {
                LOGGER.info(" size less than 2000,crawl by sort !");
                for (int i = 1; i <= 50; i++) {
                    param.setPagenow(i);
                    crawlSinglePage(param);
                    param.setOrderPx(CssciPaperParam.ASC);
                    crawlSinglePage(param);
                }
            } else {
                LOGGER.error("too much result : " + param.getUrl());
                addError(param, "too much result:" + param.getTitle());
            }
        } else if (Objects.equal(param.getStartYear(), param.getEndYear()) || pagination.getTotal() < 1000) {
            LOGGER.info("size less than 1000. journal:" + param.getTitle() + "\tstart year: "
                    + param.getStartYear() + "\tend year: " + param.getEndYear() + "\tsize: " + pagination.getTotal());
            for (int i = 1; i <= (pagination.getTotal() / PAGE_SIZE) + 1; i++) {
                param.setPagenow(i);
                crawlSinglePage(param);
            }
        } else {
            LOGGER.info("split year");
            int mid = (startYear + endYear) >> 1;
            crawlArticleBaseInfo(qkname, startYear, mid);
            crawlArticleBaseInfo(qkname, mid >= endYear ? endYear : (mid + 1), endYear);
        }
        return true;
    }

    private Map<String, String> normalHeaders(CssciPaperParam param) {
        String url = param.getUrl();
        param.getHeaders().put("Referer", url.replace(CssciPaperParam.PREFIX, CssciPaperParam.REFERER_PREFIX));
        return param.getHeaders();
    }


    private void crawlSinglePage(CssciPaperParam param) {
        try {
            String html = HttpUtils.okrHttpGet(param.getUrl(), param.getHeaders());
            List<CssciPaper> papers = artOvParser.parse(html);
            papers.forEach(cssciService::addPaper);
        } catch (Exception e) {
            addError(param, ERROR_DETAIL);
            LOGGER.error(e.getMessage());
        }

    }

    private void addError(CssciPaperParam param, String type) {
        CrawlError error = new CrawlError();
        error.setUrl(param.getUrl());
        error.setCreateDate(new Date());
        error.setMethod("GET");
        error.setSite(CssciPaperParam.PREFIX);
        error.setDeleted(false);
        error.setType(type);
        error.setBody(param.getTitle());
        crawlErrorService.addError(error);
    }

    @ApiOperation(value = "查询CSSCI论文详情信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "threadNum", value = "线程数", required = true, paramType = "query")})
    @RequestMapping(value = "crawlPaperDetail", method = RequestMethod.GET)
    public Response crawlPaperDetail(Integer threadNum) {
        Executor executor = Executors.newFixedThreadPool(threadNum);
        Pagination pagination = new Pagination();
        pagination.setPageIndex(1);
        pagination.setPageSize(Integer.MAX_VALUE);
        List<String> papers = cssciService.selectToCrawl(pagination);
        LOGGER.info("start crawl ,size:" + papers.size());
        papers.forEach(p -> executor.execute(() -> crawlPaperSingleTask(p)));
        LOGGER.info("papers:{}", papers);
        return Response.success(null);
    }

    private void crawlPaperSingleTask(String sno) {
        CssciPaperDetailParam detailParam = new CssciPaperDetailParam(resource);
        detailParam.setSno(sno);
        try {
            String html = HttpUtils.okrHttpGet(detailParam.getUrl(), detailParam.getHeaders());

            List<CssciPaper> papers = detailParser.parse(html);
            List<CssciAuthor> authors = authorParser.parse(html);
            List<CssciPaperAuthorRel> paperAuthorRelList = authors.stream().map(au -> this.genReal(au, sno)).collect(Collectors.toList());
            List<CssciCitation> cssciCitations = citationParser.parse(html);

            papers.forEach(cssciService::updatePaperSelective);
            authors.forEach(cssciService::addAuthor);
            paperAuthorRelList.forEach(cssciService::addPaperAuthorRelation);
            cssciCitations.forEach(cssciService::addCitation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CssciPaperAuthorRel genReal(CssciAuthor au, String sno) {
        CssciPaperAuthorRel rel = new CssciPaperAuthorRel();
        rel.setAuthorId(au.getId());
        rel.setSno(sno);
        return rel;
    }

    @ApiOperation(value = "查询出错的CSSCI论文信息", httpMethod = "GET", response = Response.class)
    @RequestMapping(value = "crawlErrorPaper", method = RequestMethod.GET)
    public Response crawlErrorPaper() {
        CrawlError condition = new CrawlError();
        condition.setDeleted(false);
        Pagination pagination = new Pagination();
        pagination.setPageIndex(1);
        pagination.setPageSize(Integer.MAX_VALUE);
        List<CrawlError> errorList = crawlErrorService.selectError(condition, pagination);
        Collections.sort(errorList, Comparator.comparing(CrawlError::getUrl));
        String cur = "";
        for (CrawlError error : errorList) {
            CssciPaperParam paperParam = new CssciPaperParam(resource);
            paperParam.parseUrl(error.getUrl());
            LOGGER.info(paperParam.getUrl());

            if (error.getType().equals(ERROR_DETAIL)) {
                LOGGER.info("start 2 crawl detail page");
                LOGGER.info(paperParam.getUrl());
                crawlSinglePage(paperParam);
                crawlErrorService.completed(error.getId());
                continue;
            }
            if (!error.getType().equals(cur)) {
                cur = error.getType();
            }
            LOGGER.info("start 2 crawl page");
            try {
                String prefUrl = normalizePreUrl(paperParam.getUrl());
                HttpUtils.okrHttpGet(prefUrl, paperParam.getHeaders());
                crawlErrorService.completed(error.getId());
                crawlPage(paperParam.getTitle(), paperParam);
            } catch (Exception e) {
                LOGGER.error("error:", e);
            }
        }
        return Response.success(null);
    }

    @ApiOperation(value = "查询单年超过1000的出错的CSSCI论文信息", httpMethod = "GET", response = Response.class)
    @RequestMapping(value = "crawlErrorPaperMoreThan1000", method = RequestMethod.GET)
    public Response crawlErrorPaperMoreThan1000() {
        CrawlError condition = new CrawlError();
        condition.setDeleted(false);
        Pagination pagination = new Pagination();
        pagination.setPageIndex(1);
        pagination.setPageSize(Integer.MAX_VALUE);
        List<CrawlError> errorList = crawlErrorService.selectError(condition, pagination);
        Collections.sort(errorList, Comparator.comparing(CrawlError::getUrl));
        String cur = "";
        for (CrawlError error : errorList) {
            if (!error.getType().contains("too")) {
                continue;
            }
            CssciPaperParam paperParam = new CssciPaperParam(resource);
            paperParam.parseUrl(error.getUrl());
            paperParam.setQkname(paperParam.getTitle());
            if (!error.getType().equals(cur)) {
                cur = error.getType();
            }
            LOGGER.info(paperParam.getUrl());
            try {
                crawlErrorService.completed(error.getId());
                crawlPage(paperParam.getTitle(), paperParam);
            } catch (Exception e) {
                LOGGER.error("error:{}", e);
            }
        }
        return Response.success(null);
    }

    private String normalizePreUrl(String url) {
        String prefUrl = url.replace(CssciPaperParam.PREFIX, CssciPaperParam.REFERER_PREFIX);
        prefUrl = prefUrl.replaceAll("qkname(.*?)&", "");
        prefUrl = prefUrl.replace("pagesize", "pagenum");
        return URLDecoder.decode(prefUrl);
    }
}

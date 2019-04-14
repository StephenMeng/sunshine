package team.stephen.sunshine.controller.other;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.bean.cssci.*;
import team.stephen.sunshine.model.other.bean.Pagination;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.CssciService;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.service.other.parse.ParserFactory;
import team.stephen.sunshine.service.other.parse.ParserType;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleAuthorParser;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleCitationParser;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleDetailParser;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
        String filepath = dir + "journal.txt";
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
        LogRecord.print(journal + "\t" + startYear + "\t" + endYear + "\n");
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
        CssciPaperParam param = new CssciPaperParam(resource);
        param.setTitle(qkname);
        param.setPagenow(1);
        param.setStartYear(String.valueOf(startYear));
        param.setEndYear(String.valueOf(endYear));
        param.setPageSize(50);
        LogRecord.print(param.getUrl());
        try {
            HttpResponse response = HttpUtils.httpGet(param.getUrl(), normalHeaders(param));
            String html = IOUtils.toString(response.getEntity().getContent());
            Thread.sleep(new Random().nextInt(3) * 1000);

            Pagination pagination = (Pagination) pageParser.parse(html).get(0);
            LogRecord.print(pagination.getTotal());
            if (pagination.getTotal() == null) {
                LogRecord.error("query pagination info error : " + param.getUrl());
                addError(param, ERROR_PAGE);
                return Response.error(ResultEnum.SERVER_WRONG);
            }
            if (Objects.equal(param.getStartYear(), param.getEndYear()) || pagination.getTotal() < 1000) {
                for (int i = 1; i <= pagination.getTotal() / PAGE_SIZE + 1; i++) {
                    param.setPagenow(i);
                    crawlSinglePage(param);
                }
            } else if (Objects.equal(param.getStartYear(), param.getEndYear()) || pagination.getTotal() > 1000) {
                LogRecord.error("too much result : " + param.getUrl());
                addError(param, "too much result:" + param.getTitle());
            } else {
                int mid = (startYear + endYear) >> 1;
                crawlArticleBaseInfo(qkname, startYear, mid);
                crawlArticleBaseInfo(qkname, mid >= endYear ? endYear : (mid + 1), endYear);
            }

        } catch (IOException | CrawlException e) {
            LogRecord.error(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Response.success(null);
    }

    private Map<String, String> normalHeaders(CssciPaperParam param) {
        String url = param.getUrl();
        param.getHeaders().put("Referer", url.replace(CssciPaperParam.PREFIX, CssciPaperParam.REFERER_PREFIX));
        return param.getHeaders();
    }


    private void crawlSinglePage(CssciPaperParam param) {
        try {
//            LogRecord.print(param.getUrl());
            String html = HttpUtils.okrHttpGet(param.getUrl(), param.getHeaders());
            List<CssciPaper> papers = artOvParser.parse(html);
            papers.forEach(cssciService::addPaper);
        } catch (Exception e) {
            addError(param, ERROR_DETAIL);
            e.printStackTrace();
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
        crawlErrorService.addError(error);
    }

    @ApiOperation(value = "查询CSSCI论文详情信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "threadNum", value = "线程数", required = true, paramType = "query")})
    @RequestMapping(value = "crawlPaperDetail", method = RequestMethod.GET)
    public Response crawlPaperDetail(Integer threadNum) {
        Pagination pagination = new Pagination();
        pagination.setPageIndex(1);
        pagination.setPageSize(Integer.MAX_VALUE);
        List<CssciPaper> papers = cssciService.selectPaper(null, pagination);
        papers = papers.stream().filter(p -> StringUtils.isBlank(p.getQkdm())).collect(Collectors.toList());

        Executor executor = Executors.newFixedThreadPool(threadNum);
        papers.forEach(p -> executor.execute(() -> crawlPaperSingleTask(p)));
        LogRecord.print(papers);
        return Response.success(null);
    }

    private void crawlPaperSingleTask(CssciPaper cssciPaper) {
        CssciPaperDetailParam detailParam = new CssciPaperDetailParam(resource);
        detailParam.setSno(cssciPaper.getSno());
        try {
            String html = HttpUtils.okrHttpGet(detailParam.getUrl(), detailParam.getHeaders());

            List<CssciPaper> papers = detailParser.parse(html);
            List<CssciAuthor> authors = authorParser.parse(html);
            List<CssciPaperAuthorRel> paperAuthorRelList = authors.stream().map(au -> this.genReal(au, cssciPaper.getSno())).collect(Collectors.toList());
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
}

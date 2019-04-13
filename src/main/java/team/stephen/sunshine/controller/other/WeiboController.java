package team.stephen.sunshine.controller.other;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.other.UrlConstant;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.model.other.bean.weibo.UserDetailParam;
import team.stephen.sunshine.model.other.bean.weibo.WeiboCrawlResource;
import team.stephen.sunshine.model.other.bean.weibo.WeiboSearchParam;
import team.stephen.sunshine.model.other.bean.weibo.WeiboSearchParamBuilder;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboParser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchPageNumParserImpl;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchParser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboUserDetailParser;
import team.stephen.sunshine.service.other.utils.CrawlUtils;
import team.stephen.sunshine.util.LogRecod;
import team.stephen.sunshine.util.common.CodeUtils;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.DateUtils;
import team.stephen.sunshine.util.element.TimeFormateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static team.stephen.sunshine.util.constant.CrawError.ERROR_DETAIL;
import static team.stephen.sunshine.util.constant.CrawError.ERROR_PAGE;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("other/weibo")
public class WeiboController extends BaseController {


    @Autowired
    private WeiboService weiboService;
    @Autowired
    private CrawlErrorService crawlErrorService;
    @Autowired
    private WeiboCrawlResource resource;
    private static final Pattern keywordPatter = Pattern.compile("q=(.*?)&");

    private static String cookie =
            "SINAGLOBAL=7256170564393.524.1525878160642; _s_tentry=www.gaoxiaojob.com; cross_origin_proto=SSL; login_sid_t=5f8965b4ced6e733efaccbc5044c6a6d; Apache=147931733707.84897.1552921740928; ULV=1552921740938:3:1:1:147931733707.84897.1552921740928:1550410282133; SSOLoginState=1552921834; wvr=6; UOR=,,login.sina.com.cn; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KMhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; ALF=1584973869; SCF=ApWJpYkIBCSLvQa6VugVvlZ6e-DWM2_b7Y4Eih38-j3oSjtKthyM4zXi-IQh0xqu51-rSZtBaDqs9qZ64MmU2g4.; SUB=_2A25xk-T_DeRhGeVN6lQQ9SvIyzmIHXVS6VE3rDV8PUNbmtBeLUzbkW9NTEdLmZwjFBvSroVHeNV3NGHwds9d5r-y; SUHB=0rr7s9yZ9fQv_B; WBStorage=201903242231|undefined";
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Cookie", cookie);
    }

    private int corePoolSize = 20;
    private int maximumPoolSize = 40;
    private int keeAliveTime = 30;


    @ApiOperation(value = "查询微博用户信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public Response crawlUser(int pageNum, int pageSize) {
//        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keeAliveTime,
//                TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, pageNum, pageSize);
        List<String> ouIds = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getOid).collect(Collectors.toList());
        ouIds = Lists.reverse(ouIds);
        LogRecord.print(ouIds.size());
        for (String ouid : ouIds) {
            executor.execute(() -> {
                String url = "https://weibo.com/u/" + ouid + "?refer_flag=1001030201_";
                LogRecord.print(url);
                WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
                if (config.getName() != null) {
                    weiboService.updateSelective(config);
                } else {
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
//            break;
        }
        return Response.success(ouIds.size());
    }

    @RequestMapping(value = "crawl_single_user", method = RequestMethod.GET)
    public void crawlSimple(@RequestParam("scriptUri") String scriptUri,
                            @RequestParam("id") String id,
                            @RequestParam("hasU") boolean hasU,
                            @RequestParam("start") int page,
                            @RequestParam("end") int end) {
        scriptUri = normalizingScriptUri(scriptUri, hasU);
        for (int i = 1; i <= page; i++) {
            String cookie = "YF-V5-G0=fec5de0eebb24ef556f426c61e53833b; YF-Page-G0=416186e6974c7d5349e42861f3303251; _s_tentry=weibo.com; login_sid_t=03fc48e9875be68669566f810ae42a36; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; UOR=www.baidu.com,weibo.com,www.baidu.com; Apache=4855100182428.531.1509249179926; SINAGLOBAL=4855100182428.531.1509249179926; ULV=1509249179947:1:1:1:4855100182428.531.1509249179926:; WBtopGlobal_register_version=b81eb8e02b10d728; SUB=_2A2508SFgDeRhGeVN6lQQ9SvIyzmIHXVXhxWorDV8PUNbmtBeLXTVkW8qOoRg6_2VEltLwq1b_fWUwqkVcg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KzhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; SUHB=0rr7s9yZ9aav43; ALF=1540785327; SSOLoginState=1509249328; wb_cusLike_3316155405=N";
            List<Weibo> weiboList = new ArrayList<>();
            weiboList.addAll(getFirstPage(i, scriptUri, cookie));
            weiboList.addAll(getBarPage(i, id, scriptUri, cookie));
            LogRecod.print(weiboList.size());
            insertIntoDataBase(weiboList);
        }
    }

    @RequestMapping(value = "user_cookie", method = RequestMethod.GET)
    public void crawlSimpleWithCookiewInfo(@RequestParam("scriptUri") String scriptUri,
                                           @RequestParam("id") String id,
                                           @RequestParam("hasU") boolean hasU,
                                           @RequestParam("start") int start,
                                           @RequestParam("end") int end,
                                           HttpServletRequest request) {
        scriptUri = normalizingScriptUri(scriptUri, hasU);
        String query = request.getQueryString();
        String cookie = query.substring(query.indexOf("=", query.indexOf("id")) + 1);
        for (int i = start; i <= end; i++) {
            List<Weibo> weiboList = new ArrayList<>();
            weiboList.addAll(getFirstPage(i, scriptUri, cookie));
            weiboList.addAll(getBarPage(i, id, scriptUri, cookie));
            insertIntoDataBase(weiboList);
        }
    }

    private String normalizingScriptUri(String script_uri, boolean hasU) {
        if (hasU) {
            return "/u/" + script_uri;
        } else {
            return "/" + script_uri;
        }
    }

    private List<Weibo> getBarPage(int i, String id, String script_uri, String cookie) {
        List<Weibo> weiboList = new ArrayList<>();
        for (int k = 0; k < 2; k++) {
            String url = UrlConstant.WEI_BO + "/p/aj/v6/mblog/" +
                    "mbloglist?ajwvr=6&domain=100206&is_search=0&visible=0&is_all=1&" +
                    "is_tag=0&profile_ftype=1&page=" + i +
                    "&pagebar=" + k +
                    "&pl_name=Pl_Official_MyProfileFeed__27&id=" + id +
                    "&script_uri=" + script_uri +
                    "&feed_type=0&pre_page=" + i +
                    "&domain_op=100206&__rnd=1509255368651";
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookie);
            weiboList.addAll(getBarPageWeibos(url, headers));
        }
        return weiboList;
    }

    private List<Weibo> getBarPageWeibos(String url, Map<String, String> headers) {
        List<Weibo> weiboList = new ArrayList<>();
        try {
            HttpResponse response = HttpUtils.httpGet(url, headers);
            String html = IOUtils.toString(response.getEntity().getContent(), "gbk");
            html = normalizingBarPage(html);
            weiboList.addAll(new WeiboParser().parse(html));
        } catch (Exception e) {
            insetErrorInfoIntoDataBase(url, ERROR_DETAIL);
            e.printStackTrace();
        }
        return weiboList;
    }

    private String normalizingBarPage(String html) {
        String htmlStr = CodeUtils.unicode2String(html);
        htmlStr = htmlStr.substring(htmlStr.indexOf("<div"), htmlStr.lastIndexOf("div>") + 4);
        htmlStr = htmlStr.replaceAll("\"", "");
        htmlStr = htmlStr.replaceAll("\\\\", "");
        return htmlStr;
    }

    private List<Weibo> getFirstPage(int i, String script_uri, String cookie) {
        String url = UrlConstant.WEI_BO + script_uri + "/" +
                "?is_search=0&visible=0" +
                "&is_all=1&is_tag=0&profile_ftype=1&page=" + i;
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        return getFirstPageWeibos(url, headers);
    }

    private List<Weibo> getFirstPageWeibos(String url, Map<String, String> headers) {
        try {
            HttpResponse response = HttpUtils.httpGet(url, headers);
            String html = IOUtils.toString(response.getEntity().getContent(), "utf-8");
            html = normalizingFirstPage(html);
            List<Weibo> weiboList = new WeiboParser().parse(html);
            return weiboList;
        } catch (Exception e) {
            insetErrorInfoIntoDataBase(url, ERROR_DETAIL);
        }
        return new ArrayList<>();
    }

    private String normalizingFirstPage(String html) {
        int startIndex = html.lastIndexOf("Pl_Official_MyProfileFeed__27");
        int endIndex = html.indexOf("/script", startIndex);
        html = html.substring(startIndex, endIndex);
        String htmlStr = html.substring(html.indexOf("<div"), html.lastIndexOf("div>") + 4);
        htmlStr = htmlStr.replaceAll("\"", "");
        htmlStr = htmlStr.replaceAll("\\\\", "");
        return htmlStr;
    }

    @ApiOperation(value = "根据关键词查询微博信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "keyword", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "startDate", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "endDate", required = true, dataType = "string", paramType = "query"),

    })
    @RequestMapping(value = "searchByKeyword", method = RequestMethod.GET)
    public void searchByKeyword(@RequestParam("keyword") String keyword,
                                @RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate
    ) {
        LogRecord.print("start crawl weibo : keyword is " + keyword + ",startDate is " + startDate + ",endDate is " + endDate);
        WeiboSearchParamBuilder builder = getWeiboSearchParamBuilder(keyword, startDate, endDate);
        Integer page;
        try {
            page = getTotalPage(builder.build());
        } catch (CrawlException e) {
            LogRecord.error(e);
            insetErrorInfoIntoDataBase(builder.build().getUrl(), ERROR_PAGE);
            return;
        }
        if (page < 50) {
            crawl(builder, page);
        } else if (startDate.equals(endDate)) {
            searchByKeywordByHour(keyword, startDate, startDate);
        } else

        {
            Date sd = TimeFormateUtil.parseStringToDate(startDate);
            Date ed = TimeFormateUtil.parseStringToDate(endDate);
            Date md = getMiddleDate(sd, ed);
            searchByKeyword(keyword, DateUtils.parseDateToString(sd), DateUtils.parseDateToString(md));
            searchByKeyword(keyword, DateUtils.getNextDayStr(DateUtils.parseDateToString(md)),
                    DateUtils.parseDateToString(ed));
        }

    }

    private void crawl(WeiboSearchParamBuilder builder, Integer page) {
        for (int i = 1; i < page; i++) {
            builder.setPage(i);
            List<Weibo> weibos;
            try {
                weibos = getSearchPageWeibos(builder.build());
            } catch (CrawlException e) {
                LogRecord.error(e);
                insetErrorInfoIntoDataBase(builder.build().getUrl(), ERROR_DETAIL);
                continue;
            }
            weiboService.addWeibo(weibos);
        }
    }

    private WeiboSearchParamBuilder getWeiboSearchParamBuilder(@RequestParam("keyword") String keyword, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        WeiboSearchParamBuilder builder = new WeiboSearchParamBuilder(resource);
        builder.setKeyword(keyword);
        builder.setStartDate(startDate);
        builder.setEndDate(endDate);
        return builder;
    }

    @ApiOperation(value = "根据关键词查询的微博信息，查询粒度：小时", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "keyword", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "startDate", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "endDate", required = true, dataType = "string", paramType = "query"),

    })
    @RequestMapping(value = "searchByKeywordByHour", method = RequestMethod.GET)
    public void searchByKeywordByHour(@RequestParam("keyword") String keyword,
                                      @RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate
    ) {
        LogRecord.print("start crawl weibo : keyword is " + keyword + ",startDate is " + startDate + ",endDate is " + endDate);
        Date sd = TimeFormateUtil.parseStringToDate(startDate);
        Date ed = TimeFormateUtil.parseStringToDate(endDate);
        if (Objects.isNull(ed) || Objects.isNull(sd)) {
            LogRecord.error("startDate or endDate may be unvalid");
            return;
        }
        while (!sd.after(ed)) {
            for (int sh = 0; sh <= 24; sh++) {
                String start = DateUtils.parseDateToString(sd) + ":" + format(sh);
                String end = DateUtils.parseDateToString(sd) + ":" + format(sh + 1);

                WeiboSearchParamBuilder builder = getWeiboSearchParamBuilder(keyword, start, end);
                Integer page;
                try {
                    page = getTotalPage(builder.build());
                } catch (CrawlException e) {
                    LogRecord.error(e);
                    insetErrorInfoIntoDataBase(builder.build().getUrl(), ERROR_PAGE);
                    continue;
                }
                crawl(builder, page);

            }
            sd = TimeFormateUtil.parseStringToDate(DateUtils.getNextDayStr(DateUtils.parseDateToString(sd)));
        }
    }

    private String format(int sh) {
        if (sh < 10) {
            return "0" + sh;
        }
        return String.valueOf(sh);
    }

    private Date getMiddleDate(Date sd, Date ed) {
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(sd);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(ed);
        int day = 0;
        while (calEnd.after(calStart)) {
            day++;
            calEnd.add(Calendar.DATE, -1);
        }
        calStart.add(Calendar.DATE, day / 2);
        return calStart.getTime();
    }

    private Integer getTotalPage(WeiboSearchParam param) throws CrawlException {
        List<Integer> list = (List<Integer>) CrawlUtils.getHttpResultWithFunction(param, (html) -> new WeiboSearchPageNumParserImpl().parse(html));
        return list.get(0);
    }


    private void insertIntoDataBase(List<Weibo> weiboList) {
        weiboList.forEach(weiboService::addOrUpdate);
    }

    private List<Weibo> getSearchPageWeibos(WeiboSearchParam param) throws CrawlException {
        List<Weibo> weibos = (List<Weibo>) CrawlUtils.getHttpResultWithFunction(param, (html) -> new WeiboSearchParser().parse(html));
        weibos.forEach(weibo -> weibo.setQid(param.getKeyword() + "-" + weibo.getwUrl()));
        return weibos;
    }


    private void insetErrorInfoIntoDataBase(String url, String type) {
        CrawlError error = new CrawlError();
        error.setUrl(url);
        error.setCreateDate(new Date());
        error.setMethod("GET");
        error.setSite(UrlConstant.WEI_BO);
        error.setDeleted(false);
        error.setType(type);
        crawlErrorService.addError(error);
    }

    @ApiOperation(value = "爬取用户信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thread", value = "线程数", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "爬取的数量", required = true, dataType = "string", paramType = "query")})
    @RequestMapping(value = "/crawl_user", method = RequestMethod.GET)
    public void crawlBasicInfo(@RequestParam("thread") Integer thread, @RequestParam("pageSize") Integer pageSize) {
        ExecutorService executor = Executors.newFixedThreadPool(thread);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, 1, pageSize);
        List<String> ouIds = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getOid).collect(Collectors.toList());
        LogRecord.print(ouIds.size());
        for (String oid : ouIds) {
            executor.execute(() -> {
                UserDetailParam detailParam = new UserDetailParam(resource);
                detailParam.setOid(oid);
                try {

                    List<WeiboUserConfig> userInfo = (List<WeiboUserConfig>) CrawlUtils.getHttpResultWithFunction(detailParam, (html) -> new WeiboUserDetailParser().parse(html));
                    if (!CollectionUtils.isEmpty(userInfo)) {
                        userInfo.forEach(weiboService::updateSelective);
                    }
                } catch (Exception e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }

}

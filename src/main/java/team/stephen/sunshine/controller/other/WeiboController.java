package team.stephen.sunshine.controller.other;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.other.UrlConstant;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.service.other.parse.ParserFactory;
import team.stephen.sunshine.service.other.parse.ParserType;
import team.stephen.sunshine.service.other.parse.impl.WeiboParser;
import team.stephen.sunshine.service.other.parse.impl.WeiboSearchParser;
import team.stephen.sunshine.util.LogRecod;
import team.stephen.sunshine.util.common.CodeUtils;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.DateUtils;
import team.stephen.sunshine.util.element.TimeFormateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private static final Pattern keywordPatter = Pattern.compile("q=(.*?)&");

    private static String cookie =
            "SINAGLOBAL=7256170564393.524.1525878160642; _s_tentry=www.gaoxiaojob.com; cross_origin_proto=SSL; login_sid_t=5f8965b4ced6e733efaccbc5044c6a6d; Apache=147931733707.84897.1552921740928; ULV=1552921740938:3:1:1:147931733707.84897.1552921740928:1550410282133; SSOLoginState=1552921834; wvr=6; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KMhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; ALF=1584552888; SCF=ApWJpYkIBCSLvQa6VugVvlZ6e-DWM2_b7Y4Eih38-j3oocpnJfJJraIw4-wEL42D6uAll8WmBwRZk8SyWeA5w3Y.; SUB=_2A25xlVhqDeRhGeVN6lQQ9SvIyzmIHXVS486irDV8PUNbmtBeLVDwkW9NTEdLmRtvu4Xav2QT_rVARZK-cwUiGpbQ; SUHB=0G0uvoUufz3Otp; UOR=,,login.sina.com.cn; WBStorage=201903200134|undefined";
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

    @ApiOperation(value = "查询微博用户信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "user-uri", method = RequestMethod.GET)
    public Response crawlUserByUril(int pageNum, int pageSize) {
//        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keeAliveTime,
//                TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, pageNum, pageSize);
        List<String> uris = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getUri).collect(Collectors.toList());
        LogRecord.print(uris.size());
        for (String uri : uris) {
            executor.execute(() -> {
                String url = "https://weibo.com" + uri;
                LogRecord.print(url);
                WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
                config.setUri(uri);
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
        return Response.success(uris.size());
    }

    @RequestMapping("user")
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

    @RequestMapping("user_cookie")
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
            insetErrorInfoIntoDataBase(url, e);
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
            insetErrorInfoIntoDataBase(url, e);
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
        String url = "?q=" + keyword + "&timescope=custom:" + startDate + ":" + endDate;
        String pageUrl = getPageUrl(url, 1);
        Integer page;

        page = getTotalPage(pageUrl);
        if (page == null) {
            insetErrorInfoIntoDataBase(url, null);
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (page < 50) {
            search(url, 1, page);
        } else if (startDate.equals(endDate)) {
            crawlByHour(keyword, startDate, 0, 24);

        } else {
            Date sd = TimeFormateUtil.parseStringToDate(startDate);
//            endDate = getNextDayStr(endDate);
            Date ed = TimeFormateUtil.parseStringToDate(endDate);
            Date md = getMiddleDate(sd, ed);
            searchByKeyword(keyword, DateUtils.parseDateToString(sd), DateUtils.parseDateToString(md));
            searchByKeyword(keyword, getNextDayStr(DateUtils.parseDateToString(md)),
                    DateUtils.parseDateToString(ed));
        }
    }

    @ApiOperation(value = "根据关键词查询每小时的微博信息", httpMethod = "GET", response = Response.class)
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
        while (ed.after(sd)) {
            for (int sh = 0; sh <= 24; sh++) {
                String pageUrl = "https://s.weibo.com/weibo?q=" + keyword
                        + "&xsort=hot&suball=1&timescope=custom:" + DateUtils.parseDateToString(sd) + "-" + format(sh) + ":" + DateUtils.parseDateToString(sd) +
                        "-" + format(sh + 1) + "&page=";
                Integer page = getTotalPage(pageUrl);
                if (page == null) {
                    insetErrorInfoIntoDataBase(pageUrl, null);
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                search(pageUrl, 1, page);
            }
            sd = TimeFormateUtil.parseStringToDate(getNextDayStr(DateUtils.parseDateToString(sd)));
        }
    }

    private void crawlByHour(String keyword, String startDate, int sh, int eh) {
        String pageUrl = "https://s.weibo.com/weibo?q=" + keyword
                + "&xsort=hot&suball=1&timescope=custom:" + startDate + ":" + startDate + "&page=";
        Integer page = getTotalPage(pageUrl);
        if (page == null) {
            insetErrorInfoIntoDataBase(pageUrl, null);
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (page < 50) {
            search(pageUrl, 1, page);
        } else if (sh + 1 >= eh) {
            pageUrl = "https://s.weibo.com/weibo?q=" + keyword
                    + "&xsort=hot&suball=1&timescope=custom:" + startDate + "-" + format(sh) + ":" + startDate +
                    "-" + format(eh) + "&page=";
            search(pageUrl, 1, 50);
        } else {
            int mh = (sh + eh) / 2;
            crawlByHour(keyword, startDate, sh, mh);
            crawlByHour(keyword, startDate, mh, eh);
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

    private String getNextDayStr(String dateStr) {
        Date date = DateUtils.parseStringToDate(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        return DateUtils.parseDateToString(date);
    }

    private String getNextHourStr(String startStr) {
        String dateStr = startStr.substring(0, 10);
        String hourStr = startStr.substring(11);
        int h = Integer.parseInt(hourStr);
        if (h < 23) {
            h++;
            return dateStr + "-" + h;
        }
        Date date = DateUtils.parseStringToDate(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        return DateUtils.parseDateToString(date) + "-0";
    }

    private Integer getTotalPage(String url) {
        try {
            HttpResponse response = HttpUtils.httpGet(url, headers);
            String html = IOUtils.toString(response.getEntity().getContent(), "utf-8");
            Parser parser = ParserFactory.INSTANCE.getParser(ParserType.WEIBO_SEARCH_PAGE_NUM).get();
            return (Integer) parser.parse(html).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return getTotalPage(url);
        }
    }

    @ApiOperation(value = "根据关键词和页码查询微博信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "url", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "开始", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "end", value = "结束", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(name = "search", method = RequestMethod.GET)
    public void search(@RequestParam("url") String url,
                       @RequestParam("start") int start,
                       @RequestParam("end") int end) {
        for (int i = start; i <= end; i++) {
            String pageUrl = getPageUrl(url, i);
            LogRecord.print(pageUrl);
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookie);
            List<Weibo> weiboList = getSearchPageWeibos(pageUrl, headers);
//            LogRecod.print(weiboList);
            insertIntoDataBase(weiboList);
        }
    }

    private String getPageUrl(String url, int page) {
        if (!url.contains("page=")) {
            return "http://s.weibo.com/weibo" + url + "&page=" + page;
        }
        String before = url.substring(0, url.indexOf("page="));
        return before + "page=" + page;
//        String after = url.substring(url.indexOf("&", url.indexOf("page=")) + 1);
//        return "http://s.weibo.com/weibo/" + before + after  + page;
    }

    @RequestMapping("search_cookie")
    public void searchWithCookie(@RequestParam("url") String url,
                                 @RequestParam("start") int start,
                                 @RequestParam("end") int end,
                                 HttpServletRequest request) {
        String query = request.getQueryString();
        String cookie = query.substring(query.indexOf("=", query.indexOf("end")) + 1);
        for (int i = start; i <= end; i++) {
            String pageUrl = getPageUrl(url, i);
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookie);
            List<Weibo> weiboList = getSearchPageWeibos(pageUrl, headers);
            insertIntoDataBase(weiboList);
        }
    }

    private void insertIntoDataBase(List<Weibo> weiboList) {
        weiboList.forEach(weiboService::addOrUpdate);
    }

    private List<Weibo> getSearchPageWeibos(String url, Map<String, String> headers) {
        try {
            HttpResponse response = HttpUtils.httpGet(url, headers);
            String html = IOUtils.toString(response.getEntity().getContent(), "utf-8");
//            html = normalizingSearchPage(html);
            List<Weibo> weiboList = new WeiboSearchParser().parse(html);
            weiboList.forEach(weibo -> weibo.setIndexWords(url.substring(url.indexOf("q="), url.indexOf("&"))));
            return weiboList;
        } catch (Exception e) {
            insetErrorInfoIntoDataBase(url, e);
        }
        return new ArrayList<>();
    }

    private String normalizingSearchPage(String html) {
        String sh = CodeUtils.unicode2String(html);
        html = Strings.isNullOrEmpty(sh) ? html : sh;
        LogRecord.print(html);
        int startIndex = html.lastIndexOf("pl_weibo_direct");
        int endIndex = html.indexOf("/script", startIndex);
        html = html.substring(startIndex, endIndex);
        String htmlStr = html.substring(html.indexOf("<div"), html.lastIndexOf("div>") + 4);
//        htmlStr = htmlStr.replaceAll("\"", "");
        htmlStr = htmlStr.replaceAll("\\\\", "");
        return htmlStr;
    }

    private void insetErrorInfoIntoDataBase(String url, Exception e) {
        CrawlError error = new CrawlError();
        error.setUrl(url);
        error.setCreateDate(new Date());
        error.setMethod("GET");
        error.setSite(UrlConstant.WEI_BO);
        error.setDeleted(false);
        crawlErrorService.addError(error);
    }

    @RequestMapping(value = "/crawl_user", method = RequestMethod.GET)
    public void crawlBasicInfo() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, 1, 0);
        List<String> ouIds = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getOid).collect(Collectors.toList());
        LogRecord.print(ouIds.size());
        for (String ouid : ouIds) {
            executor.execute(() -> {
                String url = "https://weibo.com/u/" + ouid;
                LogRecord.print(url);
                WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
                if (config != null) {
                    config.setOid(ouid);
                    weiboService.updateSelective(config);
                } else {

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @RequestMapping(value = "/batch_crawl_weibo_by_user", method = RequestMethod.GET)
    public Response batchCrawlWeibosByUser() {
        List<String> keywords = Lists.newArrayList("雾霾", "空气", "pm2.5");
        List<String> oids = Lists.newArrayList(
//                "1244589914",
//                "2050142347",
//                "2803301701",
                "1699432410",
                "1639498782",
                "1644114654",
                "1663937380",
                "1646068663",
                "1197161814",
                "1813080181",
                "1182391231",
                "1195031270",
                "1850988623",
                "1772412422",
                "1792419723");
        for (String keyword : keywords) {
            for (String oid : oids) {
                crawlWeiboByUser(oid, null, null, keyword, 120);
            }
        }
        return Response.success("ok");
    }

    @RequestMapping(value = "/crawl_weibo_by_user", method = RequestMethod.GET)
    public Response crawlWeiboByUser(@RequestParam("oid") String oid,
                                     @RequestParam(value = "startPage", required = false) Integer startPage,
                                     @RequestParam(value = "endPage", required = false) Integer endPage,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "internal", required = false) int internal) {
        WeiboUserConfig config = weiboService.selectUserConfig(oid);

        if (config == null) {
            return Response.success("config is null");
        }
        config.setKeyword(keyword);

        int page;
        int start = 1;
        if (startPage != null) {
            start = startPage;
        }

        if (endPage != null) {
            page = endPage;
        } else {
            page = weiboService.crawlUserWeiboPageNum(config, headers);
        }

        for (int i = start; i <= page; i++) {

            config.setKeyword(keyword);
            LogRecord.print("search begin,username:" + config.getName() + "keyword:" + keyword + ",page:" + i + ",total page:" + page);
            List<Weibo> weibos = weiboService.crawlWeibo(config, i, headers);
            weibos.forEach(weibo -> {
                weiboService.completeExtraInfo(headers, weibo);
                weibo.setIndexWords(keyword);
                try {
                    weiboService.addWeibo(weibo);
                } catch (Exception e) {
                    LogRecord.print(e.getMessage());
                }
            });
            try {
                Thread.sleep(internal * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Response.success("ok");
    }

    @RequestMapping(value = "/crawl_user_config", method = RequestMethod.GET)
    public Response crawlUserConfig(@RequestParam("uri") String uri) {
        String url = "https://weibo.com/" + uri;
        WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
        if (config != null) {
            try {
                weiboService.addWeiboUserConfig(config);
            } catch (Exception e) {
                weiboService.updateSelective(config);
                LogRecord.error(e);
            }
        }
        return Response.success(config == null ? null : config.getOid());
    }

}

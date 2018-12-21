package controller;


import com.github.pagehelper.Page;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.DateUtils;
import team.stephen.sunshine.util.element.TimeFormateUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class WeiboCrawlerTest {
    @Autowired
    private WeiboService weiboService;
    @Autowired
    private CrawlErrorService crawlErrorService;
    private static String cookie =
            "SINAGLOBAL=8490149224106.765.1520337385948; UOR=www.hankcs.com,widget.weibo.com,www.baidu.com; login_sid_t=3bda450336e5de9374c8f844fab15e49; cross_origin_proto=SSL; _s_tentry=passport.weibo.com; Apache=9039113386961.615.1545109205964; ULV=1545109205974:7:1:1:9039113386961.615.1545109205964:1530534997868; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5o275NHD95Q0e02ceK-fSh5fWs4Dqcj.i--Xi-ihiKyWi--4iKnEi-8Wi--Ri-2pi-zEi--fi-zEiK.7; ALF=1576645236; SSOLoginState=1545109237; SUB=_2A25xHA6mDeRhGeVN6lQQ9SvIyzmIHXVSaGdurDV8PUNbmtAKLU_-kW9NTEdLmRdEmNvSFEa_S-C06gV3NLgJ_92c; SUHB=0BLoa6K_CQMmWD; wvr=6";
    private static Map<String, String> headers = new HashMap<>();

    private int corePoolSize = 20;
    private int maximumPoolSize = 40;
    private int keeAliveTime = 30;

    static {
        headers.put("Cookie", cookie);
    }
    @Test
    public void testGetUserUid() {
        List<Weibo>weiboUrls=weiboService.getAll();
        Pattern pattern=Pattern.compile("\\/([0-9]*?)\\?");
        Matcher matcher;
        for (Weibo weibo : weiboUrls) {
            matcher=pattern.matcher(weibo.getwUserUrl());
            LogRecord.print(weibo.getwUserUrl());
            if(matcher.find()){
                String uid=matcher.group();
                uid=uid.replace("/","").replace("?","");
                WeiboUserConfig config=new WeiboUserConfig();
                config.setOid(uid);
                try{
                    weiboService.addWeiboUserConfig(config);
                }catch (Exception e){
                    LogRecord.print(e.getMessage());
                }
            }
        }
    }
    @Test
    public void testUpdateUserUidOfWeibo() {
        List<Weibo>weiboUrls=weiboService.getAll();
        Pattern pattern=Pattern.compile("\\/([0-9]*?)\\?");
        Matcher matcher;
        for (Weibo weibo : weiboUrls) {
            if(weibo.getwOuid()!=null&&!weibo.getwOuid().startsWith("u")){
                continue;
            }
            matcher=pattern.matcher(weibo.getwUserUrl());
            if(matcher.find()){
                String uid=matcher.group();
                uid=uid.replace("/","").replace("?","");
                Weibo w=new Weibo();
                LogRecord.print(uid+"\t"+weibo.getwUserUrl());
                w.setwUrl(weibo.getwUrl());
                w.setwOuid(uid);
                try{
                    weiboService.updateSelective(w);
                }catch (Exception e){
                    LogRecord.print(e.getMessage());
                }
            }
        }
    }
    @Test
    public void testCrawlBasicInfo() {
        String url = "https://weibo.com/u/2579500772?refer_flag=1001030201_";
        url = "https://weibo.com/nju1902?refer_flag=1001030101_";
        url = "https://weibo.com/fbb0916?refer_flag=1001030101_";
        url = "https://weibo.com/ronnieo147ROS?refer_flag=1001030101_";
//        url="https://weibo.com/u/2146154687?refer_flag=1001030101_";
//        url="https://weibo.com/leehom?refer_flag=1001030101_&is_hot=1";
        url = "https://weibo.com/chaijingkanjian?refer_flag=1001030201_";
        url="https://weibo.com/1400935015";
        WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
        if (config != null) {
            try {
                weiboService.addWeiboUserConfig(config);
            } catch (Exception e) {
                weiboService.updateSelective(config);
                LogRecord.error(e);
            }
        }
    }

    @Test
    public void insertUserId() {
        List<Weibo> weibos = weiboService.selectWeibo(null, 1, 0);
        List<String> ouIds = weibos.stream().map(Weibo::getwOuid).collect(Collectors.toList());
        removeDuplicate(ouIds);
        for (String ouId : ouIds) {
            try {
                String uri = "/u/" + ouId;
                WeiboUserConfig userConfig = new WeiboUserConfig();
                userConfig.setOid(ouId);
                userConfig.setUri(uri);
                weiboService.addWeiboUserConfig(userConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testAllCrawlBasicInfo() {
        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keeAliveTime,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
//        ExecutorService executor = Executors.newFixedThreadPool(1);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, 1, 0);
        List<String> ouIds = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getOid).collect(Collectors.toList());
        LogRecord.print(ouIds.size());
        for (String ouid : ouIds) {
//            executor.execute(() -> {
            String url = "https://weibo.com/u/" + ouid ;
            LogRecord.print(url);
            WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
            config.setOid(ouid);
            if (config != null) {
                weiboService.updateSelective(config);
            }
        }
    }

    private static void removeDuplicate(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    @Test
    public void testCrawlSearchPageNum() {
        String keyword = "邱淑贞";
        int page = 1;
        String url = "https://s.weibo.com/weibo/" + keyword
                + "&typeall=1&suball=1&page=" + page;
        try {
            LogRecord.print(weiboService.crawlWeiboSearchPageSize(url, headers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCraswlHourSearchInfo() {
        String keyword = "柴静 穹顶之下";
        keyword = "北京 红色预警";
        keyword = "阅兵蓝";
        keyword = "柴静蓝";
        keyword = "柴静雾霾调查";
        String start = "2015-02-28";
//        start = "2016-12-16";
//        start = "2015-08-22";
        String end = "2015-03-08";
        //爬多了。
//        end = "2016-12-22";
//        end = "2015-09-10";
        Date sd = TimeFormateUtil.parseStringToDate(start);
        end = getNextDayStr(end);
        Date ed = TimeFormateUtil.parseStringToDate(end);
        String startStr = start + "-0";
        while (sd.before(ed)) {
            LogRecord.print(startStr);
            sd = DateUtils.parseStringToDate(startStr.substring(0, 10));
            String pageUrl = "https://s.weibo.com/weibo/" + keyword + "&typeall=1&suball=1&timescope=custom:" + startStr + ":" + startStr + "&page=";
            crawl(keyword, pageUrl);
            startStr = getNextHourStr(startStr);
        }
    }

    @Test
    public void testCraswlDaySearchInfo() {
        String keyword = "棉纱 空气采样器";
        keyword = "柴静蓝";
        keyword = "柴静雾霾调查";
        String startStr = "2016-10-25";
        startStr = "2015-03-19";
        String endStr = "2016-10-28";
        endStr = "2015-03-20";
        Date sd = TimeFormateUtil.parseStringToDate(startStr);
        Date ed = TimeFormateUtil.parseStringToDate(endStr);
        while (sd.before(ed)) {
            sd = DateUtils.parseStringToDate(startStr);
            String pageUrl = "https://s.weibo.com/weibo/" + keyword + "&typeall=1&suball=1&timescope=custom:" + startStr + ":" + startStr + "&page=";
            crawl(keyword, pageUrl);
            LogRecord.print(startStr);
            startStr = getNextDayStr(startStr);
        }
    }


    @Test
    public void testCraswlSimpleSearchInfo() {
        String keyword = "马里兰 空气";
        keyword = "2000年以来北京空气质量 改善";
        keyword = "石家庄市民因雾霾状告环保局";
        keyword = "北大雕像戴口罩";
        keyword = "棉纱 空气采样器";
        String startStr = "2017-05-22";
        startStr = "2014-01-25";
        startStr = "2014-02-24";
        startStr = "2014-03-15";
        startStr = "2014-02-23";
        startStr = "2017-04-27";
        startStr = "2017-06-16";
        String endStr = "2017-05-30";
        endStr = "2014-01-28";
        endStr = "2014-03-01";
        endStr = "2014-03-20";
        endStr = "2014-02-28";
        endStr = "2017-04-30";
        endStr = "2017-06-19";
        String pageUrl = "https://s.weibo.com/weibo/" + keyword + "&typeall=1&suball=1&timescope=custom:" + startStr + ":" + endStr + "&page=";
        crawl(keyword, pageUrl);
    }

    @Test
    public void testCrawlErrorSearchInfo() {
        String site = "search page url";
        List<CrawlError> crawlErrors = crawlErrorService.getCrawlError(site);
        LogRecord.print(crawlErrors.size());
        for (CrawlError crawlError : crawlErrors) {
            String url = crawlError.getUrl();
            String pageUrl = url.replaceAll("page=(\\d{1,})", "page=");
            String keyword = url.substring(url.lastIndexOf("/") + 1, url.indexOf("&"));
            LogRecord.print(pageUrl + "\t" + keyword);
//            if(true){
//                continue;
//            }
            int pageNum;
            try {
                pageNum = weiboService.crawlWeiboSearchPageSize(pageUrl + 1, headers);
            } catch (Exception e) {
                e.printStackTrace();
                LogRecord.print("error pageurl :" + pageUrl);
                continue;
//                break;
            }

            LogRecord.print(pageNum);
            for (int page = 1; page <= pageNum; page++) {
                url = pageUrl + page + "&pageNum=" + pageNum;
                List<Weibo> weibos = null;
                try {
                    weibos = weiboService.crawlWeiboSearchPage(url, headers);
                } catch (Exception e) {
                    weiboService.logErrorUrl(url, "search url");
                    try {
                        while (!weiboService.verifyCode(headers)) {
                            LogRecord.print("verify code failed");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                for (Weibo weibo : weibos) {
                    weibo.setwUrl(url);
                    weiboService.completeExtraInfo(headers, weibo);
                    weibo.setIndexWords(keyword + ";");
                    boolean succcess = true;
                    try {
                        weiboService.addWeibo(weibo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        succcess = false;
                    }
                    if (succcess) {
                        continue;
                    }
                    try {
                        Weibo uw = weiboService.selectByPrimary(weibo.getwMid());
                        if (uw == null) {
                            continue;
                        }
                        if (uw.getIndexWords() == null || !uw.getIndexWords().contains(weibo.getIndexWords())) {
                            Weibo tu = new Weibo();
                            tu.setwMid(uw.getwMid());
                            tu.setIndexWords(uw.getIndexWords() + ";" + keyword);
                            weiboService.updateSelective(tu);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            crawlErrorService.completed(crawlError.getId());
        }

    }

    @Test
    public void testCrawlErrorWeiboSearchInfo() {
        String site = "search url";
        List<CrawlError> crawlErrors = crawlErrorService.getCrawlError(site);
        LogRecord.print(crawlErrors.size());
        for (CrawlError crawlError : crawlErrors) {
            String url = crawlError.getUrl();
            String keyword = url.substring(url.lastIndexOf("/") + 1, url.indexOf("&"));
            LogRecord.print(url + "\t" + keyword);
            List<Weibo> weibos = getWeibos(url);
            for (Weibo weibo : weibos) {
                weibo.setwUrl(url);
                weiboService.completeExtraInfo(headers, weibo);
                weibo.setIndexWords(keyword + ";");
                boolean succcess = true;
                try {
                    weiboService.addWeibo(weibo);
                } catch (Exception e) {
                    e.printStackTrace();
                    succcess = false;
                }
                if (succcess) {
                    continue;
                }
                try {
                    Weibo uw = weiboService.selectByPrimary(weibo.getwMid());
                    if (uw == null) {
                        continue;
                    }
                    if (uw.getIndexWords() == null || !uw.getIndexWords().contains(weibo.getIndexWords())) {
                        Weibo tu = new Weibo();
                        tu.setwMid(uw.getwMid());
                        tu.setIndexWords(uw.getIndexWords() + ";" + keyword);
                        weiboService.updateSelective(tu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            crawlErrorService.completed(crawlError.getId());
        }

    }

    private void crawl(String keyword, String pageUrl) {
        int pageNum = 1;
        try {
            pageNum = weiboService.crawlWeiboSearchPageSize(pageUrl + 1, headers);
        } catch (Exception e) {
            weiboService.logErrorUrl(pageUrl, "search page url");
        }
        LogRecord.print(pageNum);
        for (int page = 1; page <= pageNum; page++) {
            String url = pageUrl + page + "&pageNum=" + pageNum;
            List<Weibo> weibos = getWeibos(url);
            weibos.forEach(weibo -> {
                weibo.setwUrl(url);
                weiboService.completeExtraInfo(headers, weibo);
                weibo.setIndexWords(keyword + ";");
                boolean succcess = true;
                try {
                    weiboService.addWeibo(weibo);
                } catch (Exception e) {
                    e.printStackTrace();
                    succcess = false;
                }
                if (succcess) {
                    return;
                }
                try {
                    Weibo uw = weiboService.selectByPrimary(weibo.getwMid());
                    if (uw == null) {
                        return;
                    }
                    if (uw.getIndexWords() == null || !uw.getIndexWords().contains(weibo.getIndexWords())) {
                        Weibo tu = new Weibo();
                        tu.setwMid(uw.getwMid());
                        tu.setIndexWords(uw.getIndexWords() + ";" + keyword);
                        weiboService.updateSelective(tu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private List<Weibo> getWeibos(String url) {
        List<Weibo> weibos = new ArrayList<>();
        try {
            weibos = weiboService.crawlWeiboSearchPage(url, headers);
        } catch (Exception e) {
            e.printStackTrace();
            weiboService.logErrorUrl(url, "search url");
            try {
                while (!weiboService.verifyCode(headers)) {
                    LogRecord.print("verify code failed");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return weibos;
    }

    @Test
    public void testCrawlError() {
        CrawlError condition = new CrawlError();
        condition.setSite("");
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

    private String getNextDayStr(String dateStr) {
        Date date = DateUtils.parseStringToDate(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        return DateUtils.parseDateToString(date);
    }

    @Test
    public void testCrawlInfo() {
        String userUri = "/nju1902";
        userUri = "/ronnieo147ROS";
        WeiboUserConfig config = weiboService.selectUserConfig(userUri);

        if (config == null) {
            return;
        }
        int page = 14;
        List<Weibo> weibos = weiboService.crawlWeibo(config, page, headers);
        weibos.forEach(weibo -> {
            weiboService.completeExtraInfo(headers, weibo);
            try {
                weiboService.addWeibo(weibo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testCrawlAllWeibo() {
        String userUri = "/nju1902";
        userUri = "/ronnieo147ROS";
        WeiboUserConfig config = weiboService.selectUserConfig(userUri);
        weiboService.crawlWeibo(config, headers);
    }

    @Test
    public void testBigComment() {
        Weibo condition = new Weibo();
        Page<Weibo> weiboPage = weiboService.selectWeibo(condition, 1, 4);
        headers.put("X-Requested-With", "XMLHttpRequest");
        for (Weibo weibo : weiboPage) {
            List<WeiboComment> comments = weiboService.crawlWeiboComment(weibo.getwMid(), 1, headers);
            comments.forEach(comment -> {
                try {
                    weiboService.addWeiboComment(comment);
                } catch (Exception e) {
                    LogRecord.error(e);
                }
            });
        }
    }

    @Test
    public void testSmallComment() {
        headers.put("X-Requested-With", "XMLHttpRequest");
    }

    @Test
    public void testHuatiBar() {
        String url = "https://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6" +
                "&domain=100808&feed_filter=timeline&feed_sort=timeline&current_page=118" +
//                "&since_id=%7B%22last_since_id%22%3A3915205664657465%2C%22res_type%22%3A0%2C%22next_since_id%22%3A3915202690749303%7D" +
                "&page=40&pagebar=1&tab=home&pl_name=Pl_Third_App__11&id=1008084fb8dbcf8cecfbe2cb892552ed1e5c20" +
                "&script_uri=/p/1008084fb8dbcf8cecfbe2cb892552ed1e5c20/home&feed_type=1&pre_page=40&domain_op=100808";
        List<Weibo> r = weiboService.crawlWeiboPageBar(url, headers);
        LogRecord.print(r);
    }
    @Test
    public void testImportWeiboUri() throws IOException {
        String filePath="C:\\Users\\stephen\\Desktop\\weibo\\uri.txt";
        List<String>lines= Files.readLines(new File(filePath), Charsets.UTF_8);
        for(String line:lines){
            line=line.substring(line.indexOf(".com")+4,line.indexOf("?"));
            line=line.replace("/home","");
            line=line.replace("/info","");
//            LogRecord.print(line);
            WeiboUserConfig config=new WeiboUserConfig();
            config.setUri(line);
            try{
                weiboService.addWeiboUserConfig(config);
            }catch (Exception e){
                LogRecord.print(line);
            }
        }
    }
}

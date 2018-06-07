package controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.google.common.io.Files;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class WeiboCrawlerTest {
    @Autowired
    private WeiboService weiboService;
    private static String cookie ="_s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SWBSSL=usrmdinst_7; SWB=usrmdinst_15; ALF=1559849282; SSOLoginState=1528313282; SCF=ApWJpYkIBCSLvQa6VugVvlZ6e-DWM2_b7Y4Eih38-j3oLHovnRTfnSFHy3SpiZywB53HkqhV_o1jefZpb9HPxDI.; SUB=_2A252HEWTDeRhGeNJ7FYT8SnIyDiIHXVVaDBbrDV8PUNbmtANLVrbkW9NS7N__l4RArQ-48Qwz27OI77ToT5jjwxo; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=0C1SY15AkiXDpl; wvr=6; UOR=,,www.baidu.com; ULOGIN_IMG=15283356726837; WBStorage=5548c0baa42e6f3d|undefined";
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Cookie", cookie);
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
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
    public void testCraswlSearchInfo() {
        String keyword = "SVM";
//        int page = 4;
        for (int page = 1; page <= 50; page++) {
            String url = "https://s.weibo.com/weibo/" + keyword
                    + "&typeall=1&suball=1&page=" + page;

            List<Weibo> weibos = weiboService.crawlWeiboSearchPage(url, headers);
            weibos.forEach(weibo -> {
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
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
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

}

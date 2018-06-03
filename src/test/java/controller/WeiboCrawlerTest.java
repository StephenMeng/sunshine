package controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
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

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class WeiboCrawlerTest {
    @Autowired
    private WeiboService weiboService;

    @Test
    public void testCrawlBasicInfo() {
        String url = "https://weibo.com/u/2579500772?refer_flag=1001030201_";
        url = "https://weibo.com/nju1902?refer_flag=1001030101_";
        url = "https://weibo.com/fbb0916?refer_flag=1001030101_";
        url = "https://weibo.com/ronnieo147ROS?refer_flag=1001030101_";
        Map<String, String> headers = new HashMap<>();
        String cookie = "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SSOLoginState=1527608038; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1920*10801; UOR=gl.ali213.net,widget.weibo.com,www.baidu.com; WBtopGlobal_register_version=cd58c0d338fe446e; SUB=_2A252Fu7QDeRhGeVN6lQQ9SvIyzmIHXVVYkcYrDV8PUNbmtANLVnMkW9NTEdLmYRaiVRzOffkaf3SowG6O9PrIYek; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KzhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; SUHB=0S7Cl_58gugT2s; ALF=1559482880";
        headers.put("Cookie", cookie);
        WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
        if (config != null) {
            try {
                weiboService.addWeiboUserConfig(config);
            } catch (Exception e) {
                LogRecord.error(e);
            }
        }
    }

    @Test
    public void testCrawlSearchInfo() {
        String keyword = "世界杯";
        int page = 1;
        String url = "https://s.weibo.com/weibo/" + keyword
                + "&region=custom:21:1000&xsort=hot&suball=1&timescope=custom:2018-05-03-4:2018-06-03-0" +
                "&page=" + page;
        Map<String, String> headers = new HashMap<>();
        String cookie = "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SSOLoginState=1527608038; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1920*10801; UOR=gl.ali213.net,widget.weibo.com,www.baidu.com; WBtopGlobal_register_version=cd58c0d338fe446e; SUB=_2A252Fu7QDeRhGeVN6lQQ9SvIyzmIHXVVYkcYrDV8PUNbmtANLVnMkW9NTEdLmYRaiVRzOffkaf3SowG6O9PrIYek; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KzhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; SUHB=0S7Cl_58gugT2s; ALF=1559482880";
        headers.put("Cookie", cookie);
        List<Weibo> weibos = weiboService.crawlWeiboSearchPage(url, headers);

        weibos.forEach(weibo -> {
            weibo.setIndexWords(keyword + ";");
            try {
                weiboService.addWeibo(weibo);
            } catch (Exception e) {
                try {
                    Weibo uw = weiboService.selectByPrimary(weibo.getwMid());
                    if (uw != null) {
                        if (uw.getIndexWords() == null || !uw.getIndexWords().contains(weibo.getIndexWords())) {
                            Weibo tu = new Weibo();
                            tu.setwMid(uw.getwMid());
                            tu.setIndexWords(uw.getIndexWords() + ";" + keyword);
                            weiboService.updateSelective(tu);
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }


    @Test
    public void testCrawlInfo() {
//        String userUri = "/nju1902";
        String userUri = "/ronnieo147ROS";
        WeiboUserConfig config = weiboService.selectUserConfig(userUri);
        Map<String, String> headers = new HashMap<>();
        String cookie = "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SSOLoginState=1527608038; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1920*10801; UOR=gl.ali213.net,widget.weibo.com,www.baidu.com; WBtopGlobal_register_version=cd58c0d338fe446e; SUB=_2A252Fu7QDeRhGeVN6lQQ9SvIyzmIHXVVYkcYrDV8PUNbmtANLVnMkW9NTEdLmYRaiVRzOffkaf3SowG6O9PrIYek; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KzhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; SUHB=0S7Cl_58gugT2s; ALF=1559482880";
        headers.put("Cookie", cookie);
        if (config != null) {
//            for (int page = 1; page <= config.getWeiboNum() / 45 + 1; page++) {
            int page = 1;
            List<Weibo> weibos = weiboService.crawlWeiboHomePage(config, page, headers);
            weibos.forEach(weibo -> {
                try {
                    weiboService.addWeibo(weibo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });//            }
        }
    }

    @Test
    public void testBigComment() {
        Weibo condition = new Weibo();
        Page<Weibo> weiboPage = weiboService.selectWeibo(condition, 1, 4);
        Map<String, String> headers = new HashMap<>();
        String cookie = "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SSOLoginState=1527608038; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1920*10801; UOR=gl.ali213.net,widget.weibo.com,www.baidu.com; WBtopGlobal_register_version=cd58c0d338fe446e; SUB=_2A252Fu7QDeRhGeVN6lQQ9SvIyzmIHXVVYkcYrDV8PUNbmtANLVnMkW9NTEdLmYRaiVRzOffkaf3SowG6O9PrIYek; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KzhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; SUHB=0S7Cl_58gugT2s; ALF=1559482880";
        headers.put("Cookie", cookie);
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
        String url = "https://weibo.com/aj/v6/comment/small?ajwvr=6&act=list&mid=4245116224410364&uid=5774217434&isMain=true&dissDataFromFeed=%5Bobject%20Object%5D&ouid=1768409523&location=page_100206_home&comment_type=0&_t=0&__rnd=1527617833348";
        Map<String, String> headers = new HashMap<>();
        String cookie = "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SSOLoginState=1527608038; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1920*10801; UOR=gl.ali213.net,widget.weibo.com,www.baidu.com; WBtopGlobal_register_version=cd58c0d338fe446e; SUB=_2A252Fu7QDeRhGeVN6lQQ9SvIyzmIHXVVYkcYrDV8PUNbmtANLVnMkW9NTEdLmYRaiVRzOffkaf3SowG6O9PrIYek; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5KzhUgL.Foe0eKqpSK-Xeh-2dJLoIEBLxKBLB.eL122LxK.L1hzLB-2LxKnLBK2LBozLxK-LBozL1K5t; SUHB=0S7Cl_58gugT2s; ALF=1559482880";
        headers.put("Cookie", cookie);
        headers.put("X-Requested-With", "XMLHttpRequest");
    }
}

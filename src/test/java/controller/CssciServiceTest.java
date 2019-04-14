package controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.other.bean.cssci.CssciJournal;
import team.stephen.sunshine.model.other.bean.cssci.CssciPaper;
import team.stephen.sunshine.service.common.CrawlerService;
import team.stephen.sunshine.service.other.CssciService;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CssciServiceTest {
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private CssciService cssciService;

    @Test
    public void testJournal() {
        String url = "http://cssci.nju.edu.cn/js/web_message.js";
        try {
            crawlerService.get(url, html -> {
                LogRecord.print(html);
                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJournalList() {
        for (int jid = 30; jid <= 54; jid++) {
            for (int type = 1; type <= 3; type++) {
                boolean isLast = false;
                int i = 1;
                while (!isLast) {
                    String url = "http://cssci.nju.edu.cn/control/controllers.php?control=search&action=xk_qk_list&id="
                            + jid + "&type=" + type + "&pagenow=" + i;
                    try {
                        String html = HttpUtils.okrHttpGet(url);
                        LogRecord.print(url);
                        LogRecord.print(html);
                        JSONObject jsonObject = JSONObject.parseObject(html);
                        JSONArray jsonArray = jsonObject.getJSONArray("contents");
                        if (jsonArray != null) {
                            List<CssciJournal> journalList = jsonArray.toJavaList(CssciJournal.class);
                            journalList.forEach(journal -> {
                                try {
                                    cssciService.addJournal(journal);
                                } catch (Exception e) {
//                                e.printStackTrace();
                                }
                            });
                        }
                        int pageNum = jsonObject.getInteger("pagenum");
                        if (pageNum <= i) {
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }
    }

    @Test
    public void getPaperSimpleInfo() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "cssci.nju.edu.cn");
        headers.put("Cookie", "pgv_pvi=6777986048; UM_distinctid=1637c62449274b-04d8f250436551-39614807-15f900-1637c624494a65; PHPSESSID=0q9pqtlc9pc5r5dmc0rkvb1ks7");
        String url = "http://cssci.nju.edu.cn/control/controllers.php?" +
                "control=search_base&action=search_lysy&" +
                "jj=&start_year=1998&end_year=2017&nian=&juan=&qi=&xw1=&xw2=&pagesize=20&" +
                "title=%25E6%25B3%2595%25E5%25AD%25A6%252B%252B%252B8%252B%252B%252BAND%257C%257C%257C" +
                "&xkfl1=&wzlx=&" +
                "qkname=%25E6%25B3%2595%25E5%25AD%25A6&" +

                "pagenow=1&order_type=nian&order_px=DESC&search_tag=0";
        boolean isLast = false;
        int i = 1;
//        while (!isLast) {

        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            JSONObject jsonObject = JSONObject.parseObject(html);
            JSONArray jsonArray = jsonObject.getJSONArray("contents");
            if (jsonArray != null) {
                List<CssciPaper> paperList = jsonArray.toJavaList(CssciPaper.class);
                paperList.forEach(paper -> {
                    try {
                        cssciService.addPaper(paper);
                    } catch (Exception e) {
//                                e.printStackTrace();
                    }
                });
            }
            int pageNum = jsonObject.getInteger("pagenum");
            if (pageNum <= i) {
//                    break;
            }
            LogRecord.print(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
    }

    @Test
    public void getPaperDetail() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "cssci.nju.edu.cn");
        headers.put("Cookie", "pgv_pvi=6777986048; UM_distinctid=1637c62449274b-04d8f250436551-39614807-15f900-1637c624494a65; PHPSESSID=0q9pqtlc9pc5r5dmc0rkvb1ks7");
        String sno = "31D0562017020001";
        try {
            cssciService.crawlOnePaper(sno, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package controller;


import com.alibaba.fastjson.JSONObject;
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
import sun.rmi.runtime.Log;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.service.common.CrawlerService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.common.LogRecod;
import team.stephen.sunshine.util.element.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class WeiboCrawlerTest {
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private WeiboService weiboService;

    @Test
    public void testCrawlInfo() {
        int page = 1;
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; ALF=1559144038; SSOLoginState=1527608038; SUB=_2A252CQK2DeRhGeNJ7FYT8SnIyDiIHXVVf3N-rDV8PUNbmtANLVb8kW9NS7N__gCwRMiL2narbm26t4LbBe6Dy07k; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=0sqRSZVMnXFJdM; wvr=6; UOR=gl.ali213.net,widget.weibo.com,graph.qq.com; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; wb_view_log=1920*10801");
        testUrl(page, headers);
        testUrlBar(page, headers, 0);
        testUrlBar(page, headers, 1);

    }

    public void testUrl(int page, Map<String, String> headers) {
        String weiboName = "南京大学";
        String url = "https://weibo.com/nju1902?pids=Pl_Official_MyProfileFeed__28&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&ajaxpagelet=1&ajaxpagelet_v6=1";

        try {
            crawlerService.get(url, headers, html -> {
                html = html.substring(html.indexOf("view({") + 5, html.indexOf(")</script>"));
                LogRecod.print(html);
                JSONObject jsonObject = JSONObject.parseObject(html);
                String jsonResult = jsonObject.getString("html");
                parseWeibo(weiboName, jsonResult);

                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void testUrlBar(int page, Map<String, String> headers, int i) {
        String weiboName = "南京大学";
        String url = "https://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100206&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&pagebar=" + i + "&pl_name=Pl_Official_MyProfileFeed__28&id=1002061768409523&script_uri=/nju1902&feed_type=0&pre_page=2&domain_op=100206&__rnd=1527619670816";

        try {
            crawlerService.get(url, headers, html -> {
                JSONObject jsonObject = JSONObject.parseObject(html);
                String jsonResult = jsonObject.getString("data");
                parseWeibo(weiboName, jsonResult);

                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseWeibo(String weiboName, String jsonResult) {
        Document document = Jsoup.parse(jsonResult);
        Elements elements = document.select("div[tbinfo]");
        for (Element element : elements) {
            Elements ename = element.select("div[class^=WB_cardtitle_b]");
            if (ename != null && StringUtils.isNotNull(ename.html())) {
                continue;
            }
            Element wb = element.select("div[class^=WB_from S_txt]").first();
            Elements as = wb.select("a");
            String date = as.get(0).text();
            String from = null;
            try {
                from = as.get(1).text();
            } catch (Exception e) {
                LogRecod.print(as.html());
                e.printStackTrace();
            }
            Element contentE = element.select("div[class^=WB_text W_f]").first();
            Elements handles = element.select("div[class=WB_handle]").first().select("li");
            String shareCount = handles.get(1).text().replace("转发", "0");
            String commentCount = handles.get(2).text().replace("评论", "0");
            String thumbCount = handles.get(3).text().replace("ñ", "").replace("赞", "");

            Element idInfo = handles.get(2).select("a").first();
            String ouIdStr = idInfo.attr("action-data");
            ouIdStr = ouIdStr.substring(ouIdStr.indexOf("=") + 1, ouIdStr.indexOf("&"));
            String mid = idInfo.attr("suda-uatrack");
            mid = mid.substring(mid.lastIndexOf(":") + 1, mid.length());
            Weibo weibo = new Weibo();
            weibo.setwDate(date);
            weibo.setwCommentCount(commentCount);
            weibo.setwContent(contentE.text());
            weibo.setwFrom(from);
            weibo.setwUserName(weiboName);
            weibo.setwMid(mid);
            weibo.setwOuid(ouIdStr);
            weibo.setwUrl("");
            weibo.setwShareCount(shareCount);
            weibo.setwThumbCount(thumbCount);
           try {
               weiboService.addWeibo(weibo);
           }catch (Exception e){
               LogRecod.print(weibo.getwMid());
               LogRecod.print(weibo.getwContent());
           }
//                    LogRecod.print(name+"\t"+date+"\t"+from+"\t"+shareCount+"\t"+commentCount+"\t"+thumbCount+"\t"+contentE.text());
        }
    }

    @Test
    public void testBigComment() {
        String url = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4204741657577329&root_comment_max_id=4234597472349437&root_comment_max_id_type=&root_comment_ext_param=&page=2&filter=all&sum_comment_number=9&filter_tips_before=1&from=singleWeiBo&__rnd=1527616640502";
        Map<String, String> headers = new HashMap<>();
        String co = "F-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; ALF=1559144038; SSOLoginState=1527608038; SUB=_2A252CQK2DeRhGeNJ7FYT8SnIyDiIHXVVf3N-rDV8PUNbmtANLVb8kW9NS7N__gCwRMiL2narbm26t4LbBe6Dy07k; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=0sqRSZVMnXFJdM; wvr=6; UOR=gl.ali213.net,widget.weibo.com,graph.qq.com; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; wb_view_log=1920*10801";
        headers.put("Cookie", co);
        headers.put("X-Requested-With", "XMLHttpRequest");
//                "YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; ALF=1559144038; SSOLoginState=1527608038; SUB=_2A252CQK2DeRhGeNJ7FYT8SnIyDiIHXVVf3N-rDV8PUNbmtANLVb8kW9NS7N__gCwRMiL2narbm26t4LbBe6Dy07k; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=0sqRSZVMnXFJdM; wvr=6; UOR=gl.ali213.net,widget.weibo.com,graph.qq.com; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; wb_view_log=1920*10801");

        getComment(url, headers);
    }

    @Test
    public void testSmallComment() {
        String url = "https://weibo.com/aj/v6/comment/small?ajwvr=6&act=list&mid=4245116224410364&uid=5774217434&isMain=true&dissDataFromFeed=%5Bobject%20Object%5D&ouid=1768409523&location=page_100206_home&comment_type=0&_t=0&__rnd=1527617833348";
        Map<String, String> headers = new HashMap<>();
        String co = "F-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; ALF=1559144038; SSOLoginState=1527608038; SUB=_2A252CQK2DeRhGeNJ7FYT8SnIyDiIHXVVf3N-rDV8PUNbmtANLVb8kW9NS7N__gCwRMiL2narbm26t4LbBe6Dy07k; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=0sqRSZVMnXFJdM; wvr=6; UOR=gl.ali213.net,widget.weibo.com,graph.qq.com; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; wb_view_log=1920*10801";
        headers.put("Cookie", co);
        headers.put("X-Requested-With", "XMLHttpRequest");

        getComment(url, headers);
    }

    private void getComment(String url, Map<String, String> headers) {
        try {
            crawlerService.get(url, headers, html -> {
                parseComment(html);
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseComment(String html) {
        JSONObject jsonObject = JSONObject.parseObject(html);
        String jsonResult = jsonObject.getJSONObject("data").getString("html");
        Document document = Jsoup.parse(jsonResult);
        Elements elements = document.select("div[comment_id]");
        for (Element element : elements) {
            Element author = element.select("div[class=WB_text]").first();
            String authorLink = author.select("a").first().attr("href");
            String authorName = author.select("a").first().text();
            String content = author.text();
            Element wb = element.select("div[class^=WB_from S_txt]").first();
            String date = wb.text();
            Elements handle = element.select("span[class^=line S_line]");
            String reply = handle.get(2).text();
            String thumb = handle.get(3).text();
            LogRecod.print(authorName + "\t" + reply + "\t" + thumb + "\t" + date + "\t" + content + "\t" + authorLink);
        }
    }

}

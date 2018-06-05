package team.stephen.sunshine.service.other.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.other.WeiboCommentDao;
import team.stephen.sunshine.dao.other.WeiboDao;
import team.stephen.sunshine.dao.other.WeiboUserConfigDao;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.util.handler.UrlHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephen
 * @date 2018/05/30 02:33
 */
@Service
public class
WeiboServiceImpl implements WeiboService {
    @Autowired
    private WeiboDao weiboDao;
    @Autowired
    private WeiboCommentDao weiboCommentDao;
    @Autowired
    private WeiboUserConfigDao weiboUserConfigDao;
    @Autowired
    private CrawlErrorService crawlErrorService;

    private Pattern pidPatter = Pattern.compile("Pl_Official_MyProfileFeed__(.*?)\"");
    private Pattern domainPattern = Pattern.compile("domain'\\]='(.*?)'");
    private Pattern oidPattern = Pattern.compile("'oid'\\]='(.*?)'");
    private Pattern pageIdPattern = Pattern.compile("'page_id'\\]='(.*?)'");
    private Pattern titlePattern = Pattern.compile("'title_value'\\]='(.*?)'");
    private Pattern statisticPattern = Pattern.compile("\\{\"ns(.*?)>关注<(.*?)>粉丝<(.*?)>微博<(.*?)}");
    private Pattern searchResultPattern = Pattern.compile("\\{\"pid\":\"pl_weibo_direct\"(.*?)}");
    private Pattern sexPatter = Pattern.compile("\"ns\":\"pl.nav.index(.*?)}");
    private Pattern coreUserPatter = Pattern.compile("\"domid\":\"Pl_Core_UserInfo__6(.*?)}");

    private static final int WEIBO_PAGE_SIZE = 45;

    @Override
    public int addWeibo(Weibo weibo) {
        return weiboDao.insert(weibo);
    }

    @Override
    public int addWeiboComment(WeiboComment weiboComment) {
        return weiboCommentDao.insert(weiboComment);
    }

    @Override
    public int addWeiboUserConfig(WeiboUserConfig weiboUserConfig) {
        return weiboUserConfigDao.insert(weiboUserConfig);
    }

    @Override
    public WeiboUserConfig selectUserConfig(String userUri) {
        return weiboUserConfigDao.selectByPrimaryKey(userUri);
    }

    @Override
    public Page<Weibo> selectWeibo(Weibo condition, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Weibo>) weiboDao.select(condition);
    }

    @Override
    public WeiboUserConfig crawlUserConfig(String userUrl, Map<String, String> headers) {
        //获取用户的uri
        String userUri = parseUrl(userUrl);
        WeiboUserConfig config = null;
        try {
            config = new WeiboUserConfig();
            config.setUri(userUri);
            String html = HttpUtils.okrHttpGet(userUrl, headers);
            Matcher pidMatcher = pidPatter.matcher(html);
            if (pidMatcher.find()) {
                String pid = pidMatcher.group(0).replace("\\\"", "");
                config.setPids(pid);
            }
            Matcher domainMatcher = domainPattern.matcher(html);
            while (domainMatcher.find()) {
                String domain = domainMatcher.group(0).replace("domain']='", "").replace("'", "");
                config.setDomain(domain);
            }
            Matcher uidMatcher = oidPattern.matcher(html);
            while (uidMatcher.find()) {
                String uid = uidMatcher.group(0).replace("oid']='", "").replace("'", "");
                config.setOid(uid);
            }
            Matcher pageIdMatcher = pageIdPattern.matcher(html);
            while (pageIdMatcher.find()) {
                String uid = pageIdMatcher.group(0).replace("page_id']='", "").replace("'", "");
                config.setPageId(uid);
            }
            Matcher titleMatcher = titlePattern.matcher(html);
            while (titleMatcher.find()) {
                String title = titleMatcher.group(0).replace("title_value']='", "").replace("'", "");
                config.setName(title);
            }
            Matcher statisticMatcher = statisticPattern.matcher(html);
            if (statisticMatcher.find()) {
                String json = statisticMatcher.group();
                String resultHtml = getHtmlFromJson(json, "html");
                Document document = Jsoup.parse(resultHtml);
                Elements elements = document.select("strong");
                String follow = elements.get(0).text();
                String fans = elements.get(1).text();
                String weiboNum = elements.get(2).text();
                config.setWeiboNum(Integer.valueOf(weiboNum));
                config.setFansNum(Integer.valueOf(fans));
                config.setFollowNum(Integer.valueOf(follow));
            }
            Matcher sexMatcher = sexPatter.matcher(html);
            if (sexMatcher.find()) {
                String json = sexMatcher.group();
                json = "{" + json;
                Document document = Jsoup.parse(getHtmlFromJson(json, "html"));
                Element sex = document.select("span[class=S_txt1 t_link]").first();
                String sexStr = sex.text();
                if (sexStr.contains("她")) {
                    config.setSex("女");
                }
                if (sexStr.contains("他")) {
                    config.setSex("男");
                }
                LogRecord.print(sexStr);
            }
            Matcher coreUserMatcher = coreUserPatter.matcher(html);
            if (coreUserMatcher.find()) {
                String json = coreUserMatcher.group();
                LogRecord.print(json);
                json = "{" + json;
                Document document = Jsoup.parse(getHtmlFromJson(json, "html"));
                Element lvE = document.select("span[class=icon_group S_line1 W_fl]").first();
                String lv = lvE.text();
                config.setLv(lv);
                Element desE = document.select("p[class=info]").first();
                String des = desE.text();
                config.setDescription(des);
                Element detailE = document.select("div[class=WB_innerwrap]").first();
                Elements lisE = detailE.select("li");
                for (Element liE : lisE) {
                    if (liE.html().contains("cd_place")) {
                        String place = liE.text();
                        config.setPlace(place.replace("2 ", ""));
                    }
                    if (liE.html().contains("ficon_constellation ")) {
                        String birthday = liE.text();
                        config.setBirthday(birthday.replace("ö ", ""));
                    }
                    if (liE.html().contains("ficon_pinfo ")) {
                        String pinfo = liE.text();
                        config.setPinfo(pinfo.replace("Ü ", ""));
                    }
                    if (liE.html().contains("ficon_link")) {
                        String link = liE.text();
                        config.setLink(link.replace("5 ", "") + ":" + liE.select("a").first().attr("href"));
                    }
                    if (liE.html().contains("pinfo_icon_baidu")) {
                        String baike = liE.text();
                        config.setBaike(baike);
                    }

                    if (liE.html().contains("ficon_bag")) {
                        String career = liE.text();
                        config.setCareer(career.replace("3 ", ""));
                    }
                    if (liE.html().contains("ficon_cd_coupon")) {
                        String ext = liE.text();
                        config.setTag(ext.replace("T ", ""));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            logErrorUrl(userUrl, "user config");
        }
        return config;
    }

    @Override
    public List<Weibo> crawlWeibo(WeiboUserConfig config, int page, Map<String, String> headers) {
        List<Weibo> result = new ArrayList<>();
        String firstPageUrl = UrlHandler.getWeiboFirstPageUrl(config, page);
        result.addAll(crawlWeiboFirstPage(firstPageUrl, headers));
        String pageBarUrl1 = UrlHandler.getWeiboPageBarUrl(config, page, 0);
        result.addAll(crawlWeiboPageBar(pageBarUrl1, headers));
        String pageBarUrl2 = UrlHandler.getWeiboPageBarUrl(config, page, 1);
        result.addAll(crawlWeiboPageBar(pageBarUrl2, headers));
        result.forEach(w -> w.setwUserName(config.getName()));
        return result;
    }

    @Override
    public void crawlWeibo(WeiboUserConfig config, Map<String, String> headers) {
        if (config == null || config.getWeiboNum() <= 0) {
            return;
        }
        for (int page = 1; page <= (config.getWeiboNum() / WEIBO_PAGE_SIZE) + 1; page++) {
            List<Weibo> result = crawlWeibo(config, page, headers);
            result.forEach(weibo -> {
                completeExtraInfo(headers, weibo);
                addWeibo(weibo);
            });
        }
    }

    @Override
    public List<Weibo> crawlWeiboSearchPage(String url, Map<String, String> headers) {
        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            return parseSearchResult(html);
        } catch (Exception e) {
            e.printStackTrace();
            logErrorUrl(url, "search url");
        }
        return new ArrayList<>();
    }

    @Override
    public int crawlWeiboSearchPageSize(String url, Map<String, String> headers) throws IOException {
        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            Matcher matcher = searchResultPattern.matcher(html);
            if (matcher.find()) {
                String json = matcher.group();
                JSONObject jsonObject = JSONObject.parseObject(json);
                String resulthtml = jsonObject.getString("html");
                Document document = Jsoup.parse(resulthtml);
                Elements elements = document.select("div[class=W_pages]").first().select("li");
                if (elements != null) {
                    return elements.size();
                }

            }
            return 1;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public List<Weibo> crawlWeiboFirstPage(String url, Map<String, String> headers) {
        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            return parsePersonHomePage(getFirstPageHtml(html));
        } catch (Exception e) {
            LogRecord.error("获取微博firstPage失败，原因：" + e.getMessage());
            logErrorUrl(url, "weibo fist page");
        }
        return new ArrayList<>();
    }

    @Override
    public List<Weibo> crawlWeiboPageBar(String url, Map<String, String> headers) {
        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            return parsePersonHomePage(getHtmlFromJson(html, "data"));
        } catch (Exception e) {
            LogRecord.error("获取微博pagerBar失败，原因：" + e.getMessage());
            logErrorUrl(url, "weibo page bar");
        }
        return new ArrayList<>();
    }

    @Override
    public List<WeiboComment> crawlWeiboComment(String mid, int page, Map<String, String> headers) {
        String url = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=" + mid + "&page=" + page + "&filter=all&sum_comment_number=9&filter_tips_before=1&from=singleWeiBo";
        String html = null;
        try {
            html = HttpUtils.okrHttpGet(url, headers);
        } catch (IOException e) {
            LogRecord.error("获取微博评论http请求失败，原因：" + e.getMessage());

            logErrorUrl(url, "weibo comment");
        }
        List<WeiboComment> result = parseComment(html);
        result.forEach(comment -> {
            comment.setwMid(mid);
            comment.setcContent(comment.getcContent().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
        });
        return result;
    }

    @Override
    public Weibo selectByPrimary(String mid) {
        return weiboDao.selectByPrimaryKey(mid);
    }

    @Override
    public int updateSelective(Weibo tu) {
        return weiboDao.updateByPrimaryKeySelective(tu);
    }

    private List<WeiboComment> parseComment(String html) {
        List<WeiboComment> result = new ArrayList<>();
        if (html == null) {
            return result;
        }
        JSONObject jsonObject = JSONObject.parseObject(html);
        String jsonResult = jsonObject.getJSONObject("data").getString("html");
        Document document = Jsoup.parse(jsonResult);
        Elements elements = document.select("div[comment_id]");
        for (Element element : elements) {
            WeiboComment weiboComment = new WeiboComment();
            weiboComment.setcCrawlDate(new Date());
            Element author = element.select("div[class=WB_text]").first();
            String authorLink = author.select("a").first().attr("href");
            String authorName = author.select("a").first().text();
            String content = author.text();
            Element wb = element.select("div[class^=WB_from S_txt]").first();
            String date = wb.text();
            Elements handle = element.select("span[class^=line S_line]");
            String reply = handle.get(2).text();
            String thumb = handle.get(3).text();
            if (reply.contains("查看对话")) {
                reply = handle.get(3).text();
                thumb = handle.get(4).text();
            }
            weiboComment.setcUserName(authorName);
            weiboComment.setcReply(reply.replace("回复", "0"));
            weiboComment.setcThumb(thumb.replace("ñ", "").replace("赞", "0"));
            weiboComment.setcDate(date);
            weiboComment.setcContent(content);
            weiboComment.setcUserUrl(authorLink);
            result.add(weiboComment);
        }
        return result;
    }


    private String getFirstPageHtml(String html) {
        html = html.substring(html.indexOf("view({") + 5, html.indexOf(")</script>"));
        return getHtmlFromJson(html, "html");
    }


    private String getHtmlFromJson(String html, String data) {
        JSONObject jsonObject = JSONObject.parseObject(html);
        return jsonObject.getString(data);
    }

    private void logErrorUrl(String url, String site) {
        CrawlError error = new CrawlError();
        error.setDeleted(false);
        error.setSite(site);
        error.setMethod("get");
        error.setUrl(url);
        crawlErrorService.addError(error);
    }


    private List<Weibo> parsePersonHomePage(String jsonResult) throws Exception {
        List<Weibo> result = new ArrayList<>();
        if (jsonResult == null) {
            return result;
        }
        Document document = Jsoup.parse(jsonResult);
        Elements elements = document.select("div[tbinfo]");
        if (elements == null) {
            throw new Exception("html erro");
        }
        for (Element element : elements) {
            try {
                Weibo weibo = new Weibo();
                weibo.setCrawlDate(new Date());
                Elements ename = element.select("div[class^=WB_cardtitle_b]");
                if (ename != null && StringUtils.isNotNull(ename.html())) {
                    continue;
                }
                Element wb = element.select("div[class^=WB_from S_txt]").first();
                Elements as = wb.select("a");
                String date = as.get(0).text();
                String from = null;
                if (as.size() > 1) {
                    from = as.get(1).text();
                }
                Element contentE = element.select("div[class^=WB_text W_f]").first();
                parsePictures(element, weibo);
                Elements handles = element.select("div[class=WB_handle]").first().select("li");
                String shareCount = handles.get(1).text().replace("转发", "0");
                String commentCount = handles.get(2).text().replace("评论", "0");
                String thumbCount = handles.get(3).text().replace("ñ", "").replace("赞", "0");


                Element idInfo = handles.get(2).select("a").first();
                String ouIdStr = idInfo.attr("action-data");
                ouIdStr = ouIdStr.substring(ouIdStr.indexOf("=") + 1, ouIdStr.indexOf("&"));
                String mid = idInfo.attr("suda-uatrack");
                mid = mid.substring(mid.lastIndexOf(":") + 1, mid.length());
                weibo.setwDate(date);
                weibo.setwCommentCount(commentCount);
                weibo.setwContent(contentE.text());
                weibo.setwFrom(from);
                weibo.setwMid(mid);
                weibo.setwOuid(ouIdStr);
                weibo.setwUrl("");
                weibo.setwShareCount(shareCount);
                weibo.setwThumbCount(thumbCount);
                result.add(weibo);
            } catch (Exception e) {
                LogRecord.error("解析微博失败，原因：" + e.getMessage());
            }
        }
        return result;
    }

    private List<Weibo> parseSearchResult(String html) throws Exception {
        Matcher matcher = searchResultPattern.matcher(html);
        if (matcher.find()) {
            String json = matcher.group();
            JSONObject jsonObject = JSONObject.parseObject(json);
            String resulthtml = jsonObject.getString("html");
            return parseSearchResultWeibo(resulthtml);
        } else {
            throw new Exception();
        }
    }

    public List<Weibo> parseSearchResultWeibo(String html) {
        List<Weibo> result = new ArrayList<>();
        if (html == null) {
            return result;
        }
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div[tbinfo]");

        for (Element element : elements) {
            try {
                Weibo weibo = new Weibo();
                weibo.setCrawlDate(new Date());

                String ouIdStr = element.attr("tbinfo");
                ouIdStr = ouIdStr.substring(ouIdStr.indexOf("=") + 1);
                String mid = element.attr("mid");

                Element ename = element.select("div[class^=feed_content wbcon]").first().select("a").first();
                String name = ename.text();
                Element wb = element.select("div[class=feed_from W_textb]").first();
                String date = null;
                String from = null;
                try {

                    Elements as = wb.select("a");
                    date = as.get(0).text();

                    if (as.size() > 1) {
                        from = as.get(1).text();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Element contentE = element.select("p[class=comment_txt]").first();
                parsePictures(element, weibo);
                Elements handles = element.select("div[class=feed_action clearfix]").first().select("li");
                String shareCount = handles.get(1).text().replace("转发", "0");
                String commentCount = handles.get(2).text().replace("评论", "0");
                String thumbCount = handles.get(3).text().replace("ñ", "0").replace("赞", "");


                weibo.setwDate(date);
                weibo.setwCommentCount(commentCount);
                weibo.setwContent(contentE.text());
                weibo.setwFrom(from);
                weibo.setwUserName(name);
                weibo.setwMid(mid);
                weibo.setwOuid(ouIdStr);
                weibo.setwUrl("");
                weibo.setwShareCount(shareCount);
                weibo.setwThumbCount(thumbCount);
                result.add(weibo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void parsePictures(Element element, Weibo weibo) {
        try {
            Element pictureE = element.select("div[class^=WB_media_wrap]").first();
            Elements picEs = pictureE.select("img");
            StringBuilder pics = new StringBuilder();
            for (Element picE : picEs) {
                pics.append(picE.attr("src")).append(";");
            }
            weibo.setwPics(pics.toString());
        } catch (Exception e) {
            LogRecord.error("没有图片，原因：" + e.getMessage());
        }
    }

    private String parseUrl(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.contains("/u/")) {
            return url.substring(url.lastIndexOf("/u/"));
        }
        return url.substring(url.lastIndexOf("/"));
    }

    @Override
    public void completeExtraInfo(Map<String, String> headers, Weibo weibo) {
        try {
            //去除emoj表情符号
            weibo.setwContent(weibo.getwContent().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
        } catch (Exception e) {
            LogRecord.error("去除emoj表情符号失败，原因：" + e.getMessage());
        }
        try {
            //去除emoj表情符号
            weibo.setwFrom(weibo.getwFrom().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
        } catch (Exception e) {
            LogRecord.error("去除emoj表情符号失败，原因：" + e.getMessage());
        }
        if (weibo.getwContent().contains("展开全文")) {
            String fullContent = getFullContent(weibo.getwMid(), headers);
            if (fullContent != null) {
                weibo.setwContent(fullContent);
            }
        }
    }

    @Override
    public int updateSelective(WeiboUserConfig config) {
        return weiboUserConfigDao.updateByPrimaryKeySelective(config);
    }

    private String getFullContent(String s, Map<String, String> headers) {
        try {
            String url = "https://weibo.com/p/aj/mblog/getlongtext?ajwvr=6&mid=" + s;
            String res = HttpUtils.okrHttpGet(url, headers);
            JSONObject jsonObject = JSONObject.parseObject(res);
            String jsonResult = jsonObject.getJSONObject("data").getString("html");
            Document document = Jsoup.parse(jsonResult);
            return document.text();
        } catch (Exception e) {
            LogRecord.error("获取微博全文失败，原因：" + e.getMessage());

        }
        return null;
    }

}

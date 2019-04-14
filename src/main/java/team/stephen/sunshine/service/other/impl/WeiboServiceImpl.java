package team.stephen.sunshine.service.other.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import net.sourceforge.tess4j.util.ImageHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
import team.stephen.sunshine.model.other.bean.weibo.Weibo;
import team.stephen.sunshine.model.other.bean.weibo.WeiboComment;
import team.stephen.sunshine.model.other.bean.weibo.WeiboUserConfig;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.SvmPredict;
import team.stephen.sunshine.util.bean.WeiboVerifyResult;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.util.handler.UrlHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephen
 * @date 2018/05/30 02:33
 */
@Service
public class WeiboServiceImpl implements WeiboService {
    @Autowired
    private WeiboDao weiboDao;
    @Autowired
    private WeiboCommentDao weiboCommentDao;
    @Autowired
    private WeiboUserConfigDao weiboUserConfigDao;
    @Autowired
    private CrawlErrorService crawlErrorService;
    private Pattern pattern = Pattern.compile("\\/([0-9]*?)\\?");
    private Matcher matcher;
    private Pattern pidPatter = Pattern.compile("Pl_Official_MyProfileFeed__(.*?)\"");
    private Pattern domainPattern = Pattern.compile("domain'\\]='(.*?)'");
    private Pattern oidPattern = Pattern.compile("'oid'\\]='(.*?)'");
    private Pattern pageIdPattern = Pattern.compile("'page_id'\\]='(.*?)'");
    private Pattern titlePattern = Pattern.compile("'title_value'\\]='(.*?)'");
    private Pattern statisticPattern = Pattern.compile("\\{\"ns(.*?)>关注<(.*?)>粉丝<(.*?)>微博<(.*?)}");
    private Pattern searchResultPattern = Pattern.compile("\\{\"pid\":\"pl_weibo_direct\"(.*?)}");
    private Pattern sexPatter = Pattern.compile("\"ns\":\"pl.nav.index(.*?)}");
    private Pattern coreUserPatter = Pattern.compile("\"domid\":\"Pl_Core_UserInfo__6(.*?)}");

    private static final String svmBaseDir = "C:\\Users\\Stephen\\Desktop\\sunshine\\weibo\\code\\svm\\";
    private static final String svmTrainFilePath = svmBaseDir + "train\\train.txt";
    private static final String svmModelFilePath = svmBaseDir + "model\\model.txt";
    private static final String svmTestFilePath = svmBaseDir + "test\\test-%s.txt";
    private static final String svmToTestFilePath = svmBaseDir + "totest";
    private static final String svmPredictFilePath = svmBaseDir + "predict\\predict-%s.txt";
    private static final int Y_SCALE = 1000;

    private String verifyPicUrl = "http://s.weibo.com/ajax/pincode/pin";
    private String postVerifyUrl = "https://s.weibo.com/ajax/pincode/verified";

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
        WeiboUserConfig config;
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
            }
            Matcher coreUserMatcher = coreUserPatter.matcher(html);
            if (coreUserMatcher.find()) {
                try {
                    String json = coreUserMatcher.group();
                    json = "{" + json;
                    Document document = Jsoup.parse(getHtmlFromJson(json, "html"));
                    try {
                        Element lvE = document.select("span[class=icon_group S_line1 W_fl]").first();
                        String lv = lvE.text();
                        config.setLv(lv);
                    } catch (Exception e) {
                    }
                    try {
                        Element desE = document.select("p[class=info]").first();
                        String des = desE.text();
                        config.setDescription(des);
                    } catch (Exception e) {
                    }
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
                            config.setPinfo(StringUtils.filterEmoji(config.getPinfo()));
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            try {
                LogRecord.print("休息10秒钟");
                Thread.sleep(10000);

            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            return null;
        }
        if (StringUtils.isNull(config.getName())) {
            try {
                LogRecord.print("休息5秒钟");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
//        result.forEach(w -> w.setFullContentParam(config.getName()));

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
    public List<Weibo> crawlWeiboSearchPage(String url, Map<String, String> headers) throws Exception {
        String html;
        try {
            html = HttpUtils.okrHttpGet(url, headers);
        } catch (Exception e) {
            e.printStackTrace();
            logErrorUrl(url, "search url");
            return new ArrayList<>();
        }
        try {
            return parseSearchResult(html);
        } catch (Exception e) {
            throw e;

        }
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
                Elements elements = null;
                try {
                    elements = document.select("div[class=W_pages]").first().select("li");
                } catch (Exception ee) {
//                    ee.printStackTrace();
                }
                if (elements == null) {
                    List<Weibo> weibos = parseSearchResultWeibo(resulthtml);
                    if (weibos != null && weibos.size() > 0) {
                        return 1;
                    }
                }
                if (elements != null) {
                    return elements.size();
                }

            }
            return 1;
        } catch (IOException e) {
            try {
                while (!verifyCode(headers)) {
                    LogRecord.print("verify code failed");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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

    @Override
    public void logErrorUrl(String url, String site) {

        CrawlError error = new CrawlError();
        error.setUrl(url);

        int count = crawlErrorService.selectCount(error);
        if (count > 0) {
            return;
        }
        error.setSite(site);
        error.setMethod("get");
        error.setDeleted(false);
        crawlErrorService.addError(error);
    }

    @Override
    public List<String> getAllUserIdsFromWeibo() {
        return weiboDao.selectAllUserIdsFromWeibo();
    }

    @Override
    public List<String> getAllUrlsFromWeibo() {
        return weiboDao.selectAllUrlsFromWeibo();
    }

    @Override
    public Page<WeiboUserConfig> selectUserConfig(WeiboUserConfig condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<WeiboUserConfig>) weiboUserConfigDao.select(condition);
    }

    @Override
    public void addOrUpdate(Weibo weibo) {
        LogRecord.print(weibo);
        try {
            weiboDao.insert(weibo);
        } catch (Exception e) {
            LogRecord.error(e);
        }
        try {
            WeiboUserConfig userConfig = new WeiboUserConfig();
            String ouId = weibo.getwOuid();
            String uri = "/u/" + ouId;
            userConfig.setOid(ouId);
            userConfig.setUri(uri);
            addWeiboUserConfig(userConfig);

        } catch (Exception e) {
            LogRecord.error(e);
        }
    }

    @Override
    public List<Weibo> getAll() {
        return weiboDao.selectAll();
    }

    @Override
    public int crawlUserWeiboPageNum(WeiboUserConfig config, Map<String, String> headers) {
        int page = getPagFromPageBar(config, headers, 1);
        if (page == -1) {
            page = getPagFromPageBar(config, headers, 0);
        }
        return page == -1 ? 1 : page;
    }

    @Override
    public void addWeibo(List<Weibo> weibos) {
        weibos.forEach(this::addOrUpdate);
    }

    private int getPagFromPageBar(WeiboUserConfig config, Map<String, String> headers, int barIndex) {
        String pageBarUrl2 = UrlHandler.getWeiboPageBarUrl(config, 1, barIndex);
        String json = null;
        try {
            String html = HttpUtils.okrHttpGet(pageBarUrl2, headers);
            json = getHtmlFromJson(html, "data");
        } catch (Exception e) {
            LogRecord.error("获取微博pagerBar失败，原因：" + e.getMessage());
        }
        if (StringUtils.isNotNull(json)) {
            Document document = Jsoup.parse(json);
            Elements elements = document.select("a[action-type=feed_list_page_more]");
            if (elements != null && elements.size() > 0) {
                String pageStr = elements.first().attr("action-data");
                pageStr = pageStr.substring(pageStr.indexOf("countPage=") + 10);
                try {
                    return Integer.parseInt(pageStr);
                } catch (Exception e) {
                    LogRecord.print(e);
                }
            }
        }
        return -1;
    }


    private List<Weibo> parsePersonHomePage(String jsonResult) throws Exception {
        List<Weibo> result = new ArrayList<>();
        if (StringUtils.isNull(jsonResult)) {
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
                weibo.setCreateDate(new Date());
                Elements ename = element.select("div[class^=WB_cardtitle_b]");
                if (ename != null && StringUtils.isNotNull(ename.html())) {
                    continue;
                }
                Element wb = element.select("div[class^=WB_from S_txt]").first();
                Elements as = wb.select("a");
                String date = as.get(0).text();
                String url = as.get(0).attr("href");
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
                weibo.setwUrl(url);
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
                weibo.setCreateDate(new Date());

                String ouIdStr = element.attr("tbinfo");
                ouIdStr = ouIdStr.substring(ouIdStr.indexOf("=") + 1);
                if (ouIdStr.contains("&")) {
                    ouIdStr = ouIdStr.substring(0, ouIdStr.indexOf("&"));
                }
                String mid = element.attr("mid");

                Element ename = element.select("div[class^=feed_content wbcon]").first().select("a").first();
                String name = ename.text();
                Elements wbs = element.select("div[class=feed_from W_textb]");
                String date = null;
                String from = null;
                try {
                    Element wb;
                    if (wbs.size() >= 2) {
                        wb = wbs.get(1);
                    } else {
                        wb = wbs.get(0);
                    }
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

    @Override
    public WeiboVerifyResult getVerifyCodeResult(String url) {
        WeiboVerifyResult vr = new WeiboVerifyResult();
        OkHttpClient httpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.build();

        InputStream in;
        String fileName = svmBaseDir + "/test.png";
        try {
            okhttp3.Response response = httpClient.newCall(request).execute();
            List<String> setCookies = response.headers("Set-Cookie");
            String img = null;
            for (String setCookie : setCookies) {
                if (setCookie.contains("ULOGIN")) {
                    img = setCookie.substring(setCookie.indexOf("=") + 1, setCookie.indexOf(";"));
                    break;
                }
            }
            vr.setuLoginImg(img);
            in = response.body().byteStream();
            byte[] tempbytes = new byte[100];
            int byteread;
            OutputStream outputStream = new FileOutputStream(new File(fileName));
            while ((byteread = in.read(tempbytes)) != -1) {
                outputStream.write(tempbytes, 0, byteread);
            }
            outputStream.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return vr;
        }
        BufferedImage grayImage = null;
        try {
            grayImage = ImageHelper.convertImageToBinary(ImageIO.read(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        removeZaoyin(grayImage);

        BufferedWriter writer;
        String testFilePath = svmTestFilePath;
        try {
            writer = Files.newWriter(new File(testFilePath), Charsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return vr;
        }

        try {
            List<BufferedImage> subs = getBufferedImages(grayImage);
            for (BufferedImage sub : subs) {
                StringBuilder sb = getTrainData(sub, "0");
                writer.write(sb.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String predictPath = svmPredictFilePath;
        try {
            testSvmVerify(testFilePath, predictPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String code = testGenSvmResult(predictPath);
        vr.setCode(code);
        return vr;
    }

    @Override
    public boolean verifyCode(Map<String, String> headers) throws Exception {
        String cookie = headers.get("Cookie");
        if (StringUtils.isNull(cookie)) {
            throw new Exception("cookie can not be null");
        }
        WeiboVerifyResult vr = getVerifyCodeResult(verifyPicUrl);
        if (vr == null || vr.getCode() == null || vr.getuLoginImg() == null) {
            LogRecord.print("vr is null");
            return false;
        }
        LogRecord.print(vr.getCode() + "\t" + vr.getuLoginImg());
        if (vr.getCode().length() != 4) {
            LogRecord.print("vr is error");
            return false;
        }
        cookie = updateVerifyCookie(cookie, vr.getuLoginImg());

        headers.put("Cookie", cookie);
        headers.put("Referer", "https://s.weibo.com/weibo/%25E5%258D&Refer=index");
        headers.put("Cookie", cookie);
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>(4);
        body.put("secode", vr.getCode());
        body.put("type", "sass");
        body.put("pageid", "weibo");
        body.put("_t", "0");
        try {
            String res = HttpUtils.okrHttpPost(postVerifyUrl, headers, body);
            if (res.contains("retcode")) {
                LogRecord.print("verify code success");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String updateVerifyCookie(String cookie, String uLoginImg) {
        return cookie.replaceAll("ULOGIN_IMG=(\\d{1,})", "ULOGIN_IMG=" + uLoginImg);
    }

    private void testSvmVerify(String testFile, String predictPath) throws IOException {
        String[] parg = {
                //测试数据
                testFile,
                // 调用训练模型
                svmModelFilePath,
                predictPath,
                "-g", "2.0", "-c", "100", "-t", "0", "-m", "500.0", "-h", "0",
                "-b", "1", "-v", "5"};
        try {
            //预测结果
            SvmPredict.main(parg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String testGenSvmResult(String filePath) {
        List<String> result;
        List<String> codes = new ArrayList<>();
        try {
            result = Files.readLines(new File(filePath), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        try {
            for (int j = 1; j < result.size(); j++) {
                String item = result.get(j);
                if (StringUtils.isNotNull(item)) {
                    codes.add(getChar(Integer.parseInt(item.substring(0, item.indexOf(".")))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String r = Joiner.on("").join(codes);
        return r;
    }

    private String getChar(int i) {
        if (i < 10) {
            return i + "";
        }
        return String.valueOf((char) ('a' + (i - 10)));

    }

    private StringBuilder getTrainData(BufferedImage img, String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(getInt(filename)).append(" ");
        sb.append(getSvmDataFromImage(img));
        sb.append("\r\n");
        return sb;
    }

    private StringBuilder getSvmDataFromImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                sb.append(y * Y_SCALE + x).append(":").append(getWeight(img, x, y)).append(" ");
            }
        }
        return sb;
    }

    private List<BufferedImage> getBufferedImages(BufferedImage img) {
        List<BufferedImage> subImages = new ArrayList<>();
        int width = img.getWidth();

        boolean has = false;
        int startX = 0;
        int endX = 0;
        for (int x = 0; x < width; ++x) {
            try {
                if (isEmpty(img, x, true)) {
                    if (has) {
                        endX = x - 1;
                        BufferedImage sub = img.getSubimage(startX, 0, endX - startX, img.getHeight());
                        int startY = 0;
                        int endY = 0;
                        for (int y = 0; y < sub.getHeight(); y++) {
                            if (!isEmpty(sub, y, false)) {
                                startY = y;
                                break;
                            }
                        }
                        for (int y = sub.getHeight() - 1; y >= 0; y--) {
                            if (!isEmpty(sub, y, false)) {
                                endY = y;
                                break;
                            }
                        }
                        try {
                            sub = img.getSubimage(startX, startY, endX - startX, endY - startY);
                            subImages.add(sub);
                        } catch (Exception e) {
                        }
                        has = false;
                    }
                    continue;
                }
                if (!has) {
                    startX = x;
                    has = true;
                }
            } catch (Exception e) {

            }
        }
        return subImages;
    }

    private boolean isEmpty(BufferedImage image, int x, boolean column) {
        if (column) {
            for (int k = 0; k < image.getHeight(); k++) {
                if (!isWhite(image.getRGB(x, k))) {
                    return false;
                }
            }
        } else {
            for (int k = 0; k < image.getWidth(); k++) {
                if (!isWhite(image.getRGB(k, x))) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getInt(String s) {
        String f = s.substring(0, 1);
        try {
            return Integer.parseInt(f);
        } catch (Exception e) {
        }
        char c = f.charAt(0);
        return (c - 'a') + 10;
    }

    private double getWeight(BufferedImage img, int x, int y) {
        if (isWhite(img.getRGB(x, y))) {
            return 0.01;
        }
        return 0.99;
    }

    private void removeZaoyin(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (isWhite(img.getRGB(x, y))) {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    if (isZaoyin(img, x, y)) {
                        img.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        img.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }

            }
        }
    }

    private boolean isZaoyin(BufferedImage img, int x, int y) {
        return x == 0 || y == 0 || x == img.getWidth() - 1 || y == img.getHeight() - 1 || (isWhite(img.getRGB(x - 1, y - 1)) &&
                isWhite(img.getRGB(x - 1, y)) &&
                isWhite(img.getRGB(x - 1, y + 1)) &&
                isWhite(img.getRGB(x, y - 1)) &&
                isWhite(img.getRGB(x, y + 1)) &&
                isWhite(img.getRGB(x + 1, y - 1)) &&
                isWhite(img.getRGB(x + 1, y)) &&
                isWhite(img.getRGB(x + 1, y + 1)));
    }

    private static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
            return true;
        }
        return false;
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

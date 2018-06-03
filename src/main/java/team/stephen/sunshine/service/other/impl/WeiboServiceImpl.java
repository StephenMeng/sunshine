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
import team.stephen.sunshine.util.common.LogRecod;
import team.stephen.sunshine.util.element.StringUtils;

import java.io.IOException;
import java.util.*;
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

    private Pattern pidPatter = Pattern.compile("Pl_Official_MyProfileFeed__(.*?)\"");
    private Pattern domainPattern = Pattern.compile("domain'\\]='(.*?)'");
    private Pattern uidPattern = Pattern.compile("'page_id'\\]='(.*?)'");
    private Pattern titlePattern = Pattern.compile("'title_value'\\]='(.*?)'");
    private Pattern weiboNumPattern = Pattern.compile("粉丝(.*?)strong(.*?)>(.*?)<(.*?)微博");

    private String baseUrl = "https://weibo.com";

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
        String userUri = parseUrl(userUrl);
        WeiboUserConfig config = null;
        try {
            config = new WeiboUserConfig();
            config.setUri(userUri);
            String html = HttpUtils.okrHttpGet(userUrl, headers);
            Matcher pidMatcher = pidPatter.matcher(html);
            if (pidMatcher.find()) {
                String pid = pidMatcher.group(0).replace("Pl_Official_MyProfileFeed__", "").replace("\\\"", "");
                config.setPids(pid);
            }
            Matcher domainMatcher = domainPattern.matcher(html);
            while (domainMatcher.find()) {
                String domain = domainMatcher.group(0).replace("domain']='", "").replace("'", "");
                config.setDomain(domain);
            }
            Matcher uidMatcher = uidPattern.matcher(html);
            while (uidMatcher.find()) {
                String uid = uidMatcher.group(0).replace("page_id']='", "").replace("'", "");
                config.setOid(uid);
            }
            Matcher titleMatcher = titlePattern.matcher(html);
            while (titleMatcher.find()) {
                String title = titleMatcher.group(0).replace("title_value']='", "").replace("'", "");
                config.setName(title);
            }
            Matcher weiboNumMatcher = weiboNumPattern.matcher(html);
            while (weiboNumMatcher.find()) {
                String num = weiboNumMatcher.group(3);
                config.setWeiboNum(Integer.valueOf(num));
            }
        } catch (IOException e) {
            LogRecod.error(e);
            logErrorUrl(userUrl, "user config");
        }
        return config;
    }

    @Override
    public List<Weibo> crawlWeibo(WeiboUserConfig config, int page, Map<String, String> headers) {
        List<Weibo> result = new ArrayList<>();
        result.addAll(crawlFirstPage(config, page, headers));
        result.addAll(crawlPageBar(config, page, headers, 0));
        result.addAll(crawlPageBar(config, page, headers, 1));
        completeExtraInfo(headers, result);
        return result;
    }

    @Override
    public List<WeiboComment> crawlWeiboComment(String mid, int page, Map<String, String> headers) {
        String url = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=" + mid + "&page=" + page + "&filter=all&sum_comment_number=9&filter_tips_before=1&from=singleWeiBo";
        String html = null;
        try {
            html = HttpUtils.okrHttpGet(url, headers);
        } catch (IOException e) {
            LogRecod.error(e);
            logErrorUrl(url, "weibo comment");
        }
        List<WeiboComment> result = parseComment(html);
        result.forEach(comment -> {
            comment.setwMid(mid);
            comment.setcContent(comment.getcContent().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
        });
        return result;
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
            weiboComment.setcUserName(authorName);
            weiboComment.setcReply(reply.replace("回复", "0"));
            weiboComment.setcThumb(thumb.replace("ñ赞", "0"));
            weiboComment.setcDate(date);
            weiboComment.setcContent(content);
            weiboComment.setcUserUrl(authorLink);
            result.add(weiboComment);
        }
        return result;
    }

    private List<Weibo> crawlFirstPage(WeiboUserConfig config, int page, Map<String, String> headers) {
        String url = baseUrl + config.getUri() + "?pids=Pl_Official_MyProfileFeed__" + config.getPids() + "&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&ajaxpagelet=1&ajaxpagelet_v6=1";
        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            html = html.substring(html.indexOf("view({") + 5, html.indexOf(")</script>"));
            JSONObject jsonObject = JSONObject.parseObject(html);
            String jsonResult = jsonObject.getString("html");
            return parseWeibo(config.getName(), jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
            logErrorUrl(url, "weibo fist page");
        }
        return new ArrayList<>();
    }

    private List<Weibo> crawlPageBar(WeiboUserConfig config, int page, Map<String, String> headers, int i) {
        String url = baseUrl + "/p/aj/v6/mblog/mbloglist?ajwvr=6&domain="
                + config.getDomain() + "&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&pagebar=" + i + "&pl_name=Pl_Official_MyProfileFeed__"
                + config.getPids() + "&id=" + config.getOid() + "&script_uri="
                + config.getUri() + "&feed_type=0&pre_page=" + page + "&domain_op=" + config.getDomain();
        try {
            String html = HttpUtils.okrHttpGet(url, headers);
            JSONObject jsonObject = JSONObject.parseObject(html);
            String jsonResult = jsonObject.getString("data");
            return parseWeibo(config.getName(), jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
            logErrorUrl(url, "weibo page bar");
        }
        return new ArrayList<>();
    }

    private void logErrorUrl(String url, String site) {
        CrawlError error = new CrawlError();
        error.setDeleted(false);
        error.setSite(site);
        error.setMethod("get");
        error.setUrl(url);
        crawlErrorService.addError(error);
    }


    private List<Weibo> parseWeibo(String weiboName, String jsonResult) {
        List<Weibo> result = new ArrayList<>();
        Document document = Jsoup.parse(jsonResult);
        Elements elements = document.select("div[tbinfo]");

        for (Element element : elements) {
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
            try {
                Element pictureE = element.select("div[class^=WB_media_wrap]").first();
                Elements picEs = pictureE.select("img");
                StringBuilder pics = new StringBuilder();
                for (Element picE : picEs) {
                    pics.append(picE.attr("src")).append(";");
                }
                weibo.setwPics(pics.toString());
            } catch (Exception e) {
                LogRecod.error(e);
            }
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
            weibo.setwUserName(weiboName);
            weibo.setwMid(mid);
            weibo.setwOuid(ouIdStr);
            weibo.setwUrl("");
            weibo.setwShareCount(shareCount);
            weibo.setwThumbCount(thumbCount);
            result.add(weibo);
        }
        return result;
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

    private void completeExtraInfo(Map<String, String> headers, List<Weibo> weibos) {
        weibos.forEach(weibo -> {
            try {
                //去除emoj表情符号
                weibo.setwContent(weibo.getwContent().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
            } catch (Exception e) {
                LogRecod.error(e);
            }
            if (weibo.getwContent().contains("展开全文")) {
                String fullContent = getFullContent(weibo.getwMid(), headers);
                if (fullContent != null) {
                    weibo.setwContent(fullContent);
                }
            }
        });
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
            LogRecod.error(e);
        }
        return null;
    }

}

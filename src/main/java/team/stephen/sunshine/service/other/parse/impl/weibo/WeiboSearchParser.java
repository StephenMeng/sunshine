package team.stephen.sunshine.service.other.parse.impl.weibo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.service.other.parse.BaseParser;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author stephen
 * @date 2017/10/29
 */
public class WeiboSearchParser extends BaseParser {
    private static final String MID = "mid";
    private static final String UID = "uid";
    private static final Pattern MID_PATTER = Pattern.compile("mid=(.*?)&");
    private static final Pattern UID_PATTER = Pattern.compile("uid=(.*?)&");
    private static final Pattern UID_PARSE_PATTER = Pattern.compile("\\/([0-9]*?)\\?");

    @Override
    public List parse(String html) {
        List<Weibo> weiboList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements detail = document.select("div[action-type=feed_list_item]");
        for (Element element : detail) {
            Weibo weibo = new Weibo();
            parseUserInfo(element, weibo);
            parseContent(element, weibo);
            parseFrom(element, weibo);
            parseStatistics(element, weibo);
            weibo.setCreateDate(new Date());
            weibo.setwOuid(parseOid(weibo.getwUserUrl()));

            if (weibo.getwUrl() != null) {
                weiboList.add(weibo);
            }
        }
        return weiboList;
    }

    private void parseStatistics(Element element, Weibo weibo) {
        Elements interactData = element.select("div[class=card-act]").first().select("li");
        try {
            weibo.setwCollectCount(parseItem(interactData.get(0), Element::text));
            weibo.setwShareCount(parseItem(interactData.get(1), Element::text));
            weibo.setwCommentCount(parseItem(interactData.get(2), Element::text));
            weibo.setwThumbCount(parseItem(interactData.get(3), Element::text));
        } catch (Exception e) {
            LogRecord.warn("empty item for statistics:%s", e);
        }
    }

    private void parseFrom(Element element, Weibo weibo) {
        //存在分享自的情况，判断一下，选择第二个，比较挫。
        Elements pubDates = element.select("p[class=from]");
        Element pubDate = pubDates.size() >= 2 ? pubDates.get(1) : pubDates.first();
        weibo.setwDate(parseItem(pubDate, e -> e.select("a").first().text()));
        weibo.setwUrl(parseItem(pubDate, e -> e.select("a").first().attr("href")));
        weibo.setwFrom(parseItem(pubDate, e -> {
            Element source = e.select("a").get(1);
            return StringUtils.filterEmoji(source.text());
        }));
    }

    private String parseId(String idInfo, String id) {
        Matcher matcher;
        switch (id) {
            case MID:
                matcher = MID_PATTER.matcher(idInfo);
                if (matcher.find()) {
                    return matcher.group();
                }
                break;
            case UID:
                matcher = UID_PATTER.matcher(idInfo);
                if (matcher.find()) {
                    return matcher.group();
                }
                break;
            default:
                break;
        }
        return null;
    }

    private void parseContent(Element element, Weibo weibo) {
        Element wTxt = element.select("p[class=txt]").first();
        weibo.setwContent(parseItem(wTxt, Element::text));

        Element wFullTxt = element.select("p[node-type=feed_list_content_full]").first();
        weibo.setFullContentParam(parseItem(wFullTxt, Element::text));

        Element wPic = element.select("div[node-type=feed_list_media_prev]").first();

        weibo.setwPics(parseItem(wPic, this::parsePics));
        weibo.setwMid(parseItem(wPic, this::parseMid));
    }

    private String parseMid(Element e) {
        Element wPicSub = e.select("div[class=media media-piclist]").first();
        String idInfo = wPicSub.attr("action-data");
        return parseId(idInfo, MID);
    }

    private String parsePics(Element e) {
        Elements pics = e.select("img");
        StringBuilder stringBuilder = new StringBuilder();
        pics.forEach(pic -> stringBuilder.append(pic.attr("src")).append(";"));
        return stringBuilder.toString();
    }

    private void parseUserInfo(Element element, Weibo weibo) {
        Element user = element.select("div[class=info]").first();
        weibo.setwUserName(parseItem(user, e -> e.select("a[^nick-]").first().text()));
        weibo.setwUserUrl(parseItem(user, e -> e.select("a[^nick-]").first().attr("href")));
    }

    private String parseOid(String userUrl) {
        Matcher matcher = UID_PARSE_PATTER.matcher(userUrl);
        if (matcher.find()) {
            String uid = matcher.group();
            uid = uid.replace("/", "").replace("?", "");
            return uid;
        }
        return null;
    }
}

package team.stephen.sunshine.service.other.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.service.other.Parser;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by stephen on 2017/10/29.
 */
public class WeiboSearchParser implements Parser {
    public static final String MID = "mid";
    public static final String UID = "uid";
    private static final Pattern midPatter = Pattern.compile("mid=(.*?)&");
    private static final Pattern uidPatter = Pattern.compile("uid=(.*?)&");

    @Override
    public List parse(String html) {
        List<Weibo> weiboList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements detail = document.select("div[action-type=feed_list_item]");
        for (Element element : detail) {
            Weibo weibo = new Weibo();
            try {
                parseUser(element, weibo);
            } catch (Exception e) {
            }
            try {
                parseContent(element, weibo);
            } catch (Exception e) {
            }
            parseFrom(element, weibo);
            try {
                parseStatistics(element, weibo);
            } catch (Exception e) {
            }
            weibo.setCreateDate(new Date());
            if (weibo.getwUrl() != null) {
                weibo.setFullContentParam("-");
                weiboList.add(weibo);
            }
        }
        return weiboList;
    }

    private void parseStatistics(Element element, Weibo weibo) {
        Elements interactData = element.select("div[class=card-act]").first().select("li");
        Element collect = interactData.get(0);
        weibo.setwCollectCount(collect.text());
        Element share = interactData.get(1);
        weibo.setwShareCount(share.text());
        Element comment = interactData.get(2);
        weibo.setwCommentCount(comment.text());
        Element like = interactData.get(3);
        weibo.setwThumbCount(like.text());
    }

    private void parseFrom(Element element, Weibo weibo) {
        //存在分享自的情况，判断一下，选择第二个，比较挫。
        Elements pubDates = element.select("p[class=from]");
        Element pubDate=pubDates.size()>=2?pubDates.get(1):pubDates.first();
        try {
            weibo.setwDate(pubDate.select("a").first().text());
        } catch (Exception e) {
        }
        try {
            weibo.setwUrl(pubDate.select("a").first().attr("href"));
        } catch (Exception e) {
        }
        try {
            Element source = pubDate.select("a").get(1);
            weibo.setwFrom(StringUtils.filterEmoji(source.text()));
        } catch (Exception e) {
        }
    }

    private String parseId(String idInfo, String id) {
        Matcher matcher;
        switch (id) {
            case MID:
                matcher = midPatter.matcher(idInfo);
                if (matcher.find()) {
                    return matcher.group();
                }
                break;
            case UID:
                matcher = uidPatter.matcher(idInfo);
                if (matcher.find()) {
                    return matcher.group();
                }
                break;
        }
        return null;
    }

    private void parseContent(Element element, Weibo weibo) {
        Element wTxt = element.select("p[class=txt]").first();
        weibo.setwContent(wTxt.text());

        try {
            Element wPic = element.select("div[node-type=feed_list_media_prev]").first();
            Element wPicSub = wPic.select("div[class=media media-piclist]").first();
            String idInfo = wPicSub.attr("action-data");
            Elements pics = wPic.select("img");
            StringBuilder stringBuilder = new StringBuilder();
            pics.forEach(pic -> stringBuilder.append(pic.attr("src")).append(";"));
            weibo.setwPics(stringBuilder.toString());
            weibo.setwMid(parseId(idInfo, MID));
//            weibo.setwOuid(parseId(idInfo,UID));
        } catch (Exception e) {
        }
    }

    private void parseUser(Element element, Weibo weibo) {
        Element user = element.select("div[class=info]").first();
        weibo.setwUserName(user.select("a[^nick-]").first().text());
        weibo.setwUserUrl(user.select("a[^nick-]").first().attr("href"));
    }
    @Override
    public Object parseDetail(String html) {
        return null;
    }
}

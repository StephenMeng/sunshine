package team.stephen.sunshine.service.other.parse.impl.weibo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.other.bean.weibo.Weibo;
import team.stephen.sunshine.service.other.parse.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by stephen on 2017/10/29.
 */
public class WeiboParser implements Parser {
    @Override
    public List parse(String html) {
        List<Weibo> weiboList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements detail = document.select("div[tbinfo]");
        for (Element element : detail) {
            Element eLike = element.select("div[class=WB_handle]").first();
            Element screenBox = element.select("div[class=screen_box]").first();
            Weibo weibo = new Weibo();
            try {
                Element user = element.select("div[class=WB_info]").first();
                weibo.setWUserUrl(user.select("a").first().attr("href"));
            } catch (Exception e) {
            }
            try {
                String li = screenBox.select("li").first().html();
            } catch (Exception e) {
            }
            try {
                Element face = element.select("div[class=face]").first();
//                weibo.setUserAvatarUrl(face.select("img").attr("src"));
            } catch (Exception e) {
            }
            try {
                Element pubDate = element.select("div[class=WB_from]").first();
//                weibo.setPubDate(pubDate.select("a[class=S_txt2]").first().text());
//                weibo.setUrl(pubDate.select("a").first().attr("href"));
            } catch (Exception e) {
            }
            try {
                Element source = element.select("a[action-type=app_source]").first();
//                weibo.setSource(source.text());
            } catch (Exception e) {
            }
            try {
                Element content = element.select("div[class=WB_text]").first();
//                weibo.setContent(content.text());
                try {
                    weibo.setFullContentParam(content.select("a[class=WB_text_opt]").first().attr("href"));
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
            try {
                Elements pictures = element.select("li[class=WB_pic]");
                StringBuilder sb = new StringBuilder();
                pictures.forEach(e -> sb.append(e.select("img").first().attr("src") + ";"));
//                weibo.setPictureUrls(sb.toString());
            } catch (Exception e) {
            }
            try {
                Elements interactData = eLike.select("li");
                Element share = interactData.get(1);
//                weibo.setShareNum(share.text());
                Element comment = interactData.get(2);
//                weibo.setCommentNum(comment.text());

                Element like = interactData.get(3);
//                weibo.setLikeNum(like.text());
            } catch (Exception e) {
            }
            weibo.setCreateDate(new Date());
//            LogRecod.print(weibo);
            if (weibo.getWUserUrl() != null) {
                weiboList.add(weibo);
            }
        }
        return weiboList;
    }

}

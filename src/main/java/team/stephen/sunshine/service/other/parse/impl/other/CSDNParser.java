package team.stephen.sunshine.service.other.parse.impl.other;

import com.google.common.base.Joiner;
import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.ArrayList;
import java.util.List;

public class CSDNParser implements Parser<Article> {
    @Override
    public List<Article> parse(String html) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("dl[class=search-list J_search]");
        elements.forEach(element -> {
            LogRecord.print(element.text());
            String title = element.select("dt").first().text();
            String content = element.select("dd[class=search-detail]").first().text();
            String link = element.select("dd[class=search-link]").first().text();
            Article article = Article.getNewDefaultInstance();
            article.setArticleTitle(title);
            article.setArticleContent(content);
            article.setArticleLink(link);
            List<String> tags = HanLP.extractKeyword(content, 4);
            article.setArticleTag(Joiner.on(";").join(tags));
            articles.add(article);
        });
        return articles;
    }
}

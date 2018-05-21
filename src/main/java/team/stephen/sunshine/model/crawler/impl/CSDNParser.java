package team.stephen.sunshine.model.crawler.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.crawler.Parser;
import team.stephen.sunshine.util.LogRecod;

import java.util.ArrayList;
import java.util.List;

public class CSDNParser implements Parser<Article> {
    @Override
    public List<Article> parse(String html) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("dl[class=search-list J_search]");
        elements.forEach(element -> {
            LogRecod.print(element.text());
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
package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.RequestMethodEnum;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.crawler.impl.CSDNParser;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.crawler.CrawlerService;
import team.stephen.sunshine.util.RandomIDUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CrawlerServiceTest {
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SolrService solrService;

    @Test
    public void testCrawl() {
        String url = "https://so.csdn.net/so/search/s.do?p=3&q=java&t=blog&domain=&o=&s=&u=nav/engineering&l=&rbg=1";
        Map<String, String> header = new HashMap<>(1);
        header.put("Host", "so.csdn.net");
        try {
            List<Article> articles = crawlerService.get(url, header, new CSDNParser());
            articles.forEach(article -> {
                try {
                    articleService.addArticle(article);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Article tmp = articleService.getArticleByLinkId(article.getArticleLinkId());
                solrService.addArticle(tmp);

            });
            assert articles.size() == 10;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.util.RandomIDUtil;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SolrServiceTest {
    @Autowired
    private SolrService solrService;
    @Autowired
    private ArticleService articleService;

    @Test
    public void testAdd() {
        Article article = new Article();
        article.setArticleTitle("第" + RandomIDUtil.randomID(4) + "篇文档");
        article.setArticleLinkId(RandomIDUtil.randomID(16));
        article.setArticleAuthor(1);
        article.setArticleLink(" ");
        article.setArticleUpdateDate(new Date());
        article.setArticleCreateDate(new Date());
        article.setArticleAbstract("abstract");
        article.setArticleCommentCount(0);
        article.setArticleContent(RandomIDUtil.randomID(24));
        article.setArticleHadBeenPublished(false);
        article.setArticleIsPublished(false);
        article.setArticleViewCount(0);
        article.setArticlePutTop(false);
        article.setArticleTag("stephen");
        article.setArticleViewPwd("pwd");
        try {
            articleService.addArticle(article);
            article = articleService.getArticleByLinkId(article.getArticleLinkId());
            solrService.addDoc(article.getArticleId(), article.getArticleContent(), article.getArticleTag());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            solrService.deleteDocumentById(5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

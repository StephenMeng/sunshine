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
import team.stephen.sunshine.util.common.RandomIDUtil;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    @Test
    public void testAdd() {
        Article article = new Article();
        article.setArticleTitle("第三篇文档");
        article.setArticleLinkId(RandomIDUtil.randomID(16));
        article.setArticleAuthor(1);
        article.setArticleLink(" ");
        article.setArticleUpdateDate(new Date());
        article.setArticleCreateDate(new Date());
        article.setArticleAbstract("abstract");
        article.setArticleCommentCount(0);
        article.setArticleContent("SpringBoot学习笔记（五）：配置redis");
        article.setArticleHadBeenPublished(false);
        article.setArticleIsPublished(false);
        article.setArticleViewCount(0);
        article.setArticlePutTop(false);
        article.setArticleTag("stephen");
        article.setArticleViewPwd("pwd");
        int i = -1;
        try {
            i = articleService.addArticle(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert i == 1;
    }

    @Test
    public void testSelectOne() {
        Article article=articleService.selectArticleById(1L);
        assert article!=null;
    }
}

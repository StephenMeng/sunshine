package service;


import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.RandomIDUtil;

import java.util.Date;

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
        article.setArticleAuthorId(1);
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
            article = articleService.selectArticleByLinkId(article.getArticleLinkId());
            solrService.addArticle(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            solrService.deleteArticleByArticleId(5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQuery() {
        ArticleSearchCondition condition = new ArticleSearchCondition();
        condition.setPageNum(-1);
        condition.setPageSize(10);
//        condition.setArticleContent("java");
        condition.setArticleTag("集合");
        condition.setDesc(false);
        try {
            PageInfo<Article> articlePageInfo = solrService.queryArticle(condition);
            LogRecord.print(articlePageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

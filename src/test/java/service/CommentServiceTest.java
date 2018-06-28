package service;


import com.github.pagehelper.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.common.Comment;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.CommentService;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.RandomIDUtil;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    public void testSelect() {
        Page<Comment> commentPage=commentService.selectCommentOnArticle(8L,1,0);
        commentPage.forEach(comment -> {
            LogRecord.print(comment.getCommentContent());
        });
    }
}

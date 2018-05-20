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
import team.stephen.sunshine.service.book.BookService;
import team.stephen.sunshine.util.RandomIDUtil;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Test
    public void testAdd() {
    }

    @Test
    public void testSelectOne() {
    }
}

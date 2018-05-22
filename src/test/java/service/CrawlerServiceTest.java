package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.web.dto.book.BookDto;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.model.crawler.impl.CSDNParser;
import team.stephen.sunshine.model.crawler.impl.DouBanBookParser;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.book.BookService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.common.CrawlerService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private DtoTransformService dtoTransformService;
    @Autowired
    private BookService bookService;

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
                Article tmp = articleService.selectArticleByLinkId(article.getArticleLinkId());
                solrService.addArticle(tmp);

            });
            assert articles.size() == 10;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrawlDouban() {
        String url = "https://api.douban.com/v2/book/search?q=java&start=4";
        try {
            List<BookDto> bookDtos = crawlerService.get(url, null, new DouBanBookParser());
            List<Book> books = bookDtos.stream().map(dtoTransformService::bookDtoToModel).collect(Collectors.toList());
            books.forEach(bookService::addBook);
            assert books.size() == 20;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

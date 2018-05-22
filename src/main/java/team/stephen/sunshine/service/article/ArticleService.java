package team.stephen.sunshine.service.article;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.article.Article;

public interface ArticleService {
    Article getArticleById(Long articleId);

    Article getArticleByLinkId(String linkId);

    int addArticle(Article article) throws Exception;

    int updateSelective(Article article);

    Page<Article> select(Article articleCondition);

    Article selectOne(Article articleCondition);
}

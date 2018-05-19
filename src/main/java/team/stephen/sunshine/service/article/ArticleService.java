package team.stephen.sunshine.service.article;

import team.stephen.sunshine.model.article.Article;

public interface ArticleService {
    Article getArticleById(Long articleId);

    Article getArticleByLinkId(String linkId);

    int addArticle(Article article) throws Exception;

    int updateSelective(Article article);

}

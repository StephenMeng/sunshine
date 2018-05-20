package team.stephen.sunshine.service.common;

import team.stephen.sunshine.model.article.Article;

public interface SolrService {
    void addDoc(Long articleId, String articleContent, String atricleTag);

    void deleteDocumentById(Long articleId) throws Exception;

    void querySolr(String qString);

    void addArticle(Article article);
}

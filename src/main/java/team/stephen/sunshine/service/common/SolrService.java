package team.stephen.sunshine.service.common;

import com.github.pagehelper.PageInfo;
import org.apache.solr.client.solrj.SolrServerException;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.article.Article;

import java.io.IOException;

public interface SolrService {
    void addDoc(Long articleId, String articleContent, String atricleTag);

    void deleteArticleByArticleId(Long articleId) throws Exception;

    PageInfo<Article> queryArticle(ArticleSearchCondition articleSearchCondition) throws IOException, SolrServerException, NullParamException;

    void addArticle(Article article);
}

package team.stephen.sunshine.service.common;

import com.github.pagehelper.PageInfo;
import org.apache.solr.client.solrj.SolrServerException;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.article.Article;

import java.io.IOException;

/**
 * @author stephen
 */
public interface SolrService {

    /**
     * 更新索引
     *
     * @param article 文章
     * @throws SolrServerException
     * @throws IOException
     * @throws NullParamException
     */
    void update(Article article) throws SolrServerException, IOException, NullParamException;

    /**
     * 删除索引
     *
     * @param articleId 文章的id
     * @throws Exception
     */
    void deleteArticleByArticleId(Long articleId) throws Exception;

    /**
     * 查找
     *
     * @param articleSearchCondition 查找条件
     * @return
     * @throws IOException
     * @throws SolrServerException
     * @throws NullParamException
     */
    PageInfo<Article> queryArticle(ArticleSearchCondition articleSearchCondition) throws IOException, SolrServerException, NullParamException;

    /**
     * 增加索引
     *
     * @param article article
     */
    void addArticle(Article article);
}

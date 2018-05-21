package team.stephen.sunshine.service.common.impl;

import com.github.pagehelper.PageInfo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.common.SearchConditionService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.util.common.LogRecod;
import team.stephen.sunshine.util.common.PageUtil;

import java.io.IOException;
import java.util.List;

import static team.stephen.sunshine.constant.solr.Field.*;

@Service
public class SolrServiceImpl implements SolrService {
    @Autowired
    private HttpSolrClient articleSolrClient;
    @Autowired
    private SearchConditionService searchConditionService;


    /**
     * 往索引库添加文档
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void addDoc(Long articleId, String articleContent, String atricleTag) {
        //构造一篇文档
        SolrInputDocument document = new SolrInputDocument();
        //往doc中添加字段,在客户端这边添加的字段必须在服务端中有过定义
        document.addField(ARTICLE_ID.getFieldName(), articleId);
        document.addField(ARTICLE_CONTENT.getFieldName(), articleContent);
        document.addField(ARTICLE_TAG.getFieldName(), atricleTag);
        //获得一个solr服务端的请求，去提交  ,选择具体的某一个solr core
        try {
            articleSolrClient.add(document);
            articleSolrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据id从索引库删除文档
     */
    @Override
    public void deleteArticleByArticleId(Long id) throws Exception {
        //删除文档
        articleSolrClient.deleteByQuery("ARTICLE_ID:" + id);
        //删除所有的索引
        articleSolrClient.commit();
    }

    /**
     * 查询
     *
     * @throws Exception
     */
    @Override
    public PageInfo<Article> queryArticle(ArticleSearchCondition condition) throws IOException, SolrServerException, NullParamException {
        SolrQuery query;
        try {
            query = searchConditionService.articleConditionToSolrQuery(condition);
        } catch (NullParamException e) {
            throw e;
        }
        //获取查询结果
        QueryResponse response;
        try {
            response = articleSolrClient.query(query);
        } catch (SolrServerException | IOException e) {
            throw e;
        }
        //得到实体对象
        long total = response.getResults().getNumFound();
        List<Article> tmpLists = response.getBeans(Article.class);
        if (tmpLists != null && tmpLists.size() > 0) {
            tmpLists.forEach(a -> {
                LogRecod.print(a.getArticleContent());
            });
        }
        PageInfo<Article> page = PageUtil.listToPageInfo(tmpLists, total, condition.getPageNum(), condition.getPageSize());
        return page;
    }

    @Override
    public void addArticle(Article article) {
        addDoc(article.getArticleId(), article.getArticleContent(), article.getArticleTag());
    }
}

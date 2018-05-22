package team.stephen.sunshine.service.common.impl;

import com.github.pagehelper.PageInfo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.common.SearchConditionService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.util.common.PageUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static team.stephen.sunshine.constant.solr.Field.*;

/**
 * @author stephen
 * @date 2018/5/22
 */
@Service
public class SolrServiceImpl implements SolrService {
    @Autowired
    private HttpSolrClient articleSolrClient;
    @Autowired
    private SearchConditionService searchConditionService;
    @Autowired
    private ArticleService articleService;

    @Override
    public void update(Article article) throws SolrServerException, IOException, NullParamException {
        ArticleSearchCondition condition = new ArticleSearchCondition();
        condition.setArticleId(article.getArticleId());
        QueryResponse response = getQueryResponse(condition);
        if (response.getResults().size() > 0) {
            for (SolrDocument doc : response.getResults()) {
                String id = doc.getFieldValue(ID.getFieldName()).toString();
                //构造一篇文档
                add(article, id);
            }
        }

    }

    /**
     * 根据id从索引库删除文档
     */
    @Override
    public void deleteArticleByArticleId(Long id) throws Exception {
        //删除文档
        articleSolrClient.deleteByQuery(ARTICLE_ID.getFieldName() + ":" + id);
        //执行
        articleSolrClient.commit();
    }

    /**
     * 查询
     *
     * @throws Exception
     */
    @Override
    public PageInfo<Article> queryArticle(ArticleSearchCondition condition) throws IOException, SolrServerException, NullParamException {
        QueryResponse response = getQueryResponse(condition);
        //得到实体对象
        long total = response.getResults().getNumFound();
        List<Article> tmpLists = response.getBeans(Article.class);
        tmpLists = tmpLists.stream().map(article -> article = articleService.selectArticleById(article.getArticleId()))
                .collect(Collectors.toList());
        return PageUtil.listToPageInfo(tmpLists, total, condition.getPageNum(), condition.getPageSize());
    }

    private QueryResponse getQueryResponse(ArticleSearchCondition condition) throws NullParamException, SolrServerException, IOException {
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
        return response;
    }

    @Override
    public void addArticle(Article article) {
        add(article, null);
    }

    private void add(Article article, String id) {
        SolrInputDocument document = new SolrInputDocument();
        //往doc中添加字段,在客户端这边添加的字段必须在服务端中有过定义
        if (StringUtils.isNotNull(id)) {
            document.addField(ID.getFieldName(), id);
        }
        document.addField(ARTICLE_ID.getFieldName(), article.getArticleId());
        document.addField(ARTICLE_CONTENT.getFieldName(), article.getArticleContent());
        document.addField(ARTICLE_TAG.getFieldName(), article.getArticleTag());
        document.addField(ARTICLE_PRIVATE.getFieldName(), article.getPrivate());
        document.addField(ARTICLE_DELETED.getFieldName(), article.getDeleted());
        //获得一个solr服务端的请求，去提交
        try {
            articleSolrClient.add(document);
            articleSolrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }
}

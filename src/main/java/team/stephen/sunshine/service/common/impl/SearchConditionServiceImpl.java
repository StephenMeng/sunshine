package team.stephen.sunshine.service.common.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.constant.solr.Field;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.service.common.SearchConditionService;
import team.stephen.sunshine.util.element.StringUtils;

import static team.stephen.sunshine.constant.solr.Field.*;

@Service
public class SearchConditionServiceImpl implements SearchConditionService {
    @Override
    public SolrQuery articleConditionToSolrQuery(ArticleSearchCondition articleSearchCondition) throws NullParamException {
        if (articleSearchCondition == null) {
            throw new NullParamException("articleSearchCondition is null");
        }
        SolrQuery query = new SolrQuery();
        StringBuilder sb = parseQ(articleSearchCondition);
        //设置solr查询参数
        query.set("q", sb.toString());//相关查询，比如某条数据某个字段含有周、星、驰三个字  将会查询出来 ，这个作用适用于联想查询

        //参数fq, 给query增加过滤查询条件
//        queryArticle.addFilterQuery("id:[0 TO 9]");//id为0-4

        //给query增加布尔过滤条件
        //queryArticle.addFilterQuery("description:演员");  //description字段中含有“演员”两字的数据

        //参数df,给query设置默认搜索域
//        queryArticle.set("df", "name");

        //参数sort,设置返回结果的排序规则
        if (articleSearchCondition.getSort() != null && Field.containsField(articleSearchCondition.getSort())) {
            query.setSort(articleSearchCondition.getSort(),
                    articleSearchCondition.getDesc() != null && !articleSearchCondition.getDesc()
                            ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
        }
        if (articleSearchCondition.getPageSize() == null || articleSearchCondition.getPageSize() <= 0) {
            articleSearchCondition.setPageSize(10);
        }
        if (articleSearchCondition.getPageNum() == null || articleSearchCondition.getPageNum() <= 0) {
            articleSearchCondition.setPageNum(1);
        }
        int pageSize = articleSearchCondition.getPageSize();
        int pageNum = (articleSearchCondition.getPageNum() - 1) * pageSize;
        //设置分页参数
        query.setStart(pageNum);
        //每一页多少值
        query.setRows(pageSize);
        return query;
    }

    private StringBuilder parseQ(ArticleSearchCondition articleSearchCondition) {
        StringBuilder sb = new StringBuilder();
        if (articleSearchCondition.getArticleId() != null) {
            sb.append(ARTICLE_ID.getFieldName()).append(":").append(articleSearchCondition.getArticleId());
        }
        if (StringUtils.isNotNull(articleSearchCondition.getArticleContent())) {
            if (StringUtils.isNotBlank(sb.toString())) {
                sb.append(" AND ");
            }
            sb.append(ARTICLE_CONTENT.getFieldName()).append(":").append(articleSearchCondition.getArticleContent());
        }
        if (StringUtils.isNotNull(articleSearchCondition.getArticleTag())) {
            if (StringUtils.isNotBlank(sb.toString())) {
                sb.append(" AND ");
            }
            sb.append(ARTICLE_TAG.getFieldName()).append(":").append(articleSearchCondition.getArticleTag());
        }
        if (articleSearchCondition.getPrivate() != null) {
            if (StringUtils.isNotBlank(sb.toString())) {
                sb.append(" AND ");
            }
            sb.append(ARTICLE_PRIVATE.getFieldName()).append(":").append(articleSearchCondition.getPrivate());
        }
        if (articleSearchCondition.getDeleted() != null) {
            if (StringUtils.isNotBlank(sb.toString())) {
                sb.append(" AND ");
            }
            sb.append(ARTICLE_DELETED.getFieldName()).append(":").append(articleSearchCondition.getDeleted());
        }
        return sb;
    }
}

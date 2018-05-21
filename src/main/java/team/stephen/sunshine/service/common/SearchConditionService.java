package team.stephen.sunshine.service.common;

import org.apache.solr.client.solrj.SolrQuery;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.NullParamException;

public interface SearchConditionService {
    SolrQuery articleConditionToSolrQuery(ArticleSearchCondition articleSearchCondition) throws  NullParamException;
}

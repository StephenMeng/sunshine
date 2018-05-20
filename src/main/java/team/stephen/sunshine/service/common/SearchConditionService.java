package team.stephen.sunshine.service.common;

import org.apache.solr.client.solrj.SolrQuery;
import team.stephen.sunshine.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.exception.SolrQueryException;

public interface SearchConditionService {
    SolrQuery articleConditionToSolrQuery(ArticleSearchCondition articleSearchCondition) throws SolrQueryException;
}

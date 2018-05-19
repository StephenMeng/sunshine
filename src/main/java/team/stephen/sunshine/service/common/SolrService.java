package team.stephen.sunshine.service.common;

public interface SolrService {
    void addDoc(Long articleId, String articleContent, String atricleTag);

    void deleteDocumentById(Long articleId) throws Exception;

    void querySolr(String qString);
}

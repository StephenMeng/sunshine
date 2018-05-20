package team.stephen.sunshine.conf;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stephen
 * @date 2017/7/15
 */
@Configuration
public class SolrConfiguration {
    @Bean
    public HttpSolrClient httpSolrClient() {
        return new HttpSolrClient.Builder(HostConfig.SOLR_ARTICLE_URL)
                .build();
    }
}

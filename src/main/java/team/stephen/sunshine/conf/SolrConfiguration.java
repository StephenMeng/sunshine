package team.stephen.sunshine.conf;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.stephen.sunshine.constant.WebConfig;

/**
 * @author stephen
 * @date 2017/7/15
 */
@Configuration
public class SolrConfiguration {
    @Bean
    public HttpSolrClient httpSolrClient() {
        return new HttpSolrClient.Builder(WebConfig.SOLR_ARTICLE_URL)
                .build();
    }
}

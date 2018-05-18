package team.stephen.sunshine.conf;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.stephen.sunshine.constant.WebConfig;

/**
 * Created by stephen on 2017/7/15.
 */
@Configuration
public class SolrConfiguration {
    @Bean
    public HttpSolrClient httpSolrClient(){
        HttpSolrClient solrClient=new HttpSolrClient(WebConfig.SOLR_URL);
        return  solrClient;
    }
}

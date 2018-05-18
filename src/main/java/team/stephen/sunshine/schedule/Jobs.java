package team.stephen.sunshine.schedule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import team.stephen.sunshine.util.solr.SolrAdapter;

public class Jobs {
    @Autowired
    private SolrAdapter solrAdapter;

    @Scheduled(cron = "0 1 0 0 0 0")
    public void test() {

    }

}

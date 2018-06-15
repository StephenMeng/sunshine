package team.stephen.sunshine.conf;

/**
 * Created by stephen on 2017/7/15.
 */
public class GloableConfig {
    //是不是生产环境
    public static final boolean production=false;
//    public static final boolean production=true;

    public static final boolean sendEmail = true;
    public static final boolean ftpService = false;

    public static final String SOLR_ARTICLE_URL = "http://localhost:8983/solr/article";

    public static final String REDIS_URL = "localhost";
    public static final Integer REDIS_PORT = 6379;

    public static final String AVATAR_URI = "\\avatar";
    public static final String PICTURE_URI = "\\picture";

}

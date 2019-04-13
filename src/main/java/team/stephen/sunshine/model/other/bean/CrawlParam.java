package team.stephen.sunshine.model.other.bean;

import team.stephen.sunshine.exception.CrawlException;

import java.util.Map;

/**
 * @author Stephen
 * @date 2019/03/18 23:44
 */
public interface CrawlParam {

    String getUrl() throws CrawlException;

    Map<String, String> getHeaders();

    String getEncode();

    void parseUrl(String url);
}

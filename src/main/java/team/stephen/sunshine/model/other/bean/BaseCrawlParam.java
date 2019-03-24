package team.stephen.sunshine.model.other.bean;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Stephen
 * @date 2019/03/18 23:43
 */
public abstract class BaseCrawlParam implements CrawlParam {
    protected String url;
    protected Map<String, String> headers = Maps.newHashMap();

}

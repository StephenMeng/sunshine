package team.stephen.sunshine.model.other.bean;

import com.google.common.collect.Maps;
import team.stephen.sunshine.model.other.bean.weibo.WeiboCrawlResource;

import java.util.Map;

/**
 * @author Stephen
 * @date 2019/03/18 23:43
 */
public abstract class BaseCrawlParam implements CrawlParam {
    protected String url;
    protected String encode = "utf-8";
    protected Map<String, String> headers = Maps.newHashMap();

    private BaseCrawlResource resource;

    public BaseCrawlParam(BaseCrawlResource resource) {
        this.resource = resource;
        parseResource(resource);
    }

    private void parseResource(BaseCrawlResource resource) {
        headers.put("Cookie", resource.getCookie());
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getEncode() {
        return encode;
    }
}

package team.stephen.sunshine.model.other.bean.weibo;

import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.bean.BaseCrawlParam;
import team.stephen.sunshine.util.element.StringUtils;

/**
 * @author Stephen
 * @date 2019/03/18 23:46
 */
public class UserDetailParam extends BaseCrawlParam {
    public static final String PREFIX = "https://weibo.com/u/";
    private String oid;

    public UserDetailParam(WeiboCrawlResource resource) {
        super(resource);
    }

    @Override
    public String getUrl() throws CrawlException {
        if (StringUtils.isNull(oid)) {
            throw new CrawlException("oid is empty!");
        }
        StringBuilder sb = new StringBuilder(PREFIX);
        sb.append(oid);
        return sb.toString();
    }

    @Override
    public void parseUrl(String url) {
        //todo parse Url
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
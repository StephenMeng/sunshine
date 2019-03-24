package team.stephen.sunshine.model.other.bean.weibo;

import team.stephen.sunshine.model.other.bean.BaseCrawlParam;
import team.stephen.sunshine.util.element.DateUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author Stephen
 * @date 2019/03/18 23:46
 */
public class WeiboSearchParam extends BaseCrawlParam {
    private static final String PREFIX = "http://s.weibo.com/weibo";
    private String keyword;
    private Date startDate;
    private Date endDate;


    @Override
    public String getUrl() {
        StringBuilder sb = new StringBuilder(PREFIX);
        sb.append("?q=");
        sb.append(keyword);
        if (startDate != null) {
            String sd = DateUtils.parseDateToString(startDate);
            sb.append("timescope=custom:");
            sb.append(sd);
            if (endDate != null) {
                sb.append(":");
                sb.append(DateUtils.parseDateToString(endDate));
            }
        }
        return sb.toString();
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }
}

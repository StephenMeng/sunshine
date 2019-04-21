package team.stephen.sunshine.model.other.bean.weibo;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.bean.BaseCrawlParam;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.DateUtils;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.Date;

/**
 * @author Stephen
 * @date 2019/03/18 23:46
 */
@Data
public class WeiboSearchParam extends BaseCrawlParam implements Cloneable {
    private static final String PREFIX = "http://s.weibo.com/weibo";
    private static final String AND = "&";
    String keyword;

    AdvanceType type;
    String startDate;
    String endDate;

    String encode;

    int page;

    public WeiboSearchParam() {
        super(null);
    }

    public WeiboSearchParam(WeiboCrawlResource resource) {
        super(resource);
    }

    @Override
    public String getUrl() {
        if (StringUtils.isNull(keyword)) {
            throw new RuntimeException("key word is empty!");
        }
        StringBuilder sb = new StringBuilder(PREFIX);
        assembleKeyword(sb);
        assembleDate(sb);
        assembleType(sb);
        assemblePage(sb);
        return sb.toString();
    }

    private void assemblePage(StringBuilder sb) {
        sb.append(AND);
        sb.append("page=");
        sb.append(page);
    }

    private void assembleType(StringBuilder sb) {
        if (type == null) {
            type = AdvanceType.ALL;
        }
        sb.append(AND);
        sb.append(type.getKey());
        sb.append("=");
        sb.append(type.getValue());
        sb.append(AND);
        sb.append("suball=1");
    }

    private void assembleKeyword(StringBuilder sb) {
        sb.append("?q=");
        sb.append(keyword);
    }

    private void assembleDate(StringBuilder sb) {
        if (startDate != null) {
            sb.append(AND);
            sb.append("timescope=custom:");
            sb.append(startDate);
            if (endDate != null) {
                sb.append(":");
                sb.append(endDate);
            }
        }
    }

    @Override
    public void parseUrl(String url) {
        //todo parse Url
    }

    @Override
    public String getEncode() {
        return encode;
    }

    public int getPage() {
        return page;
    }

    @Override
    public WeiboSearchParam clone() {
        WeiboSearchParam param = null;
        try {
            param = (WeiboSearchParam) super.clone();
        } catch (CloneNotSupportedException e) {
            LogRecord.error("CloneNotSupportedException");
        }
        BeanUtils.copyProperties(this, param);
        return param;
    }
}

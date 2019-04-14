package team.stephen.sunshine.model.other.bean.cssci;

import org.springframework.beans.BeanUtils;
import team.stephen.sunshine.model.other.bean.BaseCrawlParam;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.net.URLEncoder;

/**
 * @author Stephen
 * @date 2019/03/18 23:46
 */
public class CssciArticleDetailParam extends BaseCrawlParam implements Cloneable {
    public static final String PREFIX = "http://cssci.nju.edu.cn/control/controllers.php?control=search&action=source_id&id=";
    String sno;

    public CssciArticleDetailParam() {
        super(null);
    }

    public CssciArticleDetailParam(CssciCrawlResource resource) {
        super(resource);
    }

    @Override
    public String getUrl() {
        if (StringUtils.isBlank(sno)) {
            throw new RuntimeException("key word is empty!");
        }
        StringBuilder sb = new StringBuilder(PREFIX);
        sb.append(sno);
        return sb.toString();
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    @Override
    public void parseUrl(String url) {

    }


    @Override
    public CssciArticleDetailParam clone() {
        CssciArticleDetailParam param = null;
        try {
            param = (CssciArticleDetailParam) super.clone();
        } catch (CloneNotSupportedException e) {
            LogRecord.error("CloneNotSupportedException");
        }
        BeanUtils.copyProperties(this, param);
        return param;
    }
}

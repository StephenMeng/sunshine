package team.stephen.sunshine.model.other.bean.cssci;

import org.springframework.beans.BeanUtils;
import team.stephen.sunshine.model.other.bean.BaseCrawlParam;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Stephen
 * @date 2019/03/18 23:46
 */
public class CssciPaperParam extends BaseCrawlParam implements Cloneable {
    public static final String PREFIX = "http://cssci.nju.edu.cn/control/controllers.php?control=search_base&action=search_lysy";
    public static final String REFERER_PREFIX = "http://cssci.nju.edu.cn/ly_search_view.html?";
    public static final String ASC = "ASC";
    private static final String AND = "&";
    private static final String ADD_8 = "+++8";
    String qkname;
    String title;
    String startYear;
    String endYear;
    String orderType = "nian";
    String orderPx = "DESC";
    String xkfl;
    int pageSize = 50;
    int pagenow;

    public CssciPaperParam() {
        super(null);
    }

    public CssciPaperParam(CssciCrawlResource resource) {
        super(resource);
        getHeaders().put("Connection", "keep-alive");
        getHeaders().put("Host", "cssci.nju.edu.cn");
        getHeaders().put("Content-Type", "application/x-www-form-urlencoded");
        getHeaders().put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        getHeaders().put("Referer", "http://cssci.nju.edu.cn/ly_search_view.html?title=%E6%B3%95%E5%AD%A6+++8&xkfl1=&qkname=%E6%B3%95%E5%AD%A6&wzlx=&xw1=&xw2=&jj=&nian=&juan=&qi=&start_year=1998&end_year=2018&order_type=nian&order_px=DESC&pagenum=20");
        getHeaders().put("X-Requested-With", "XMLHttpRequest");
    }

    @Override
    public String getUrl() {
        if (StringUtils.isBlank(qkname) && StringUtils.isBlank(title)) {
            throw new RuntimeException("key word is empty!");
        }
        StringBuilder sb = new StringBuilder(PREFIX);
        assembleTitle(sb);
        assembleQkname(sb);
        assembleDate(sb);
        assembleSort(sb);
        assemblePage(sb);
        assembleXkfl(sb);
        assembleOther(sb);
        return sb.toString();
    }

    private void assembleXkfl(StringBuilder sb) {
        sb.append("&xkfl1=");
        if (!StringUtils.isBlank(xkfl)) {
            sb.append(xkfl);
        }
    }

    private void assembleTitle(StringBuilder sb) {
        if (!StringUtils.isBlank(title)) {
            sb.append(AND).append("title=").append(URLEncoder.encode(URLEncoder.encode(title + ADD_8)));
        }
    }

    private void assembleOther(StringBuilder sb) {
        sb.append(AND).append("session_key=479&search_tag=1");
        sb.append(AND).append("rand=0.6675326233034267");
        sb.append("&nian=&juan=&qi=&xw1=&xw2=");
        sb.append("&wzlx=");
    }

    private void assemblePage(StringBuilder sb) {
        sb.append(AND).append("pagenow=").append(pagenow);
        sb.append(AND).append("pagesize=").append(pageSize);
    }

    private void assembleSort(StringBuilder sb) {
        sb.append(AND).append("order_type=").append(orderType);
        sb.append(AND).append("order_px=").append(orderPx);
    }

    private void assembleQkname(StringBuilder sb) {
        if (!StringUtils.isBlank(qkname)) {
            sb.append(AND).append("qkname=").append(URLEncoder.encode(URLEncoder.encode(qkname)));
//            sb.append(AND).append("qkname=").append((qkname));

        }
    }

    private void assembleDate(StringBuilder sb) {
        if (startYear != null) {
            sb.append(AND);
            sb.append("start_year=");
            sb.append(startYear);
            if (endYear != null) {
                sb.append(AND).append("end_year=");
                sb.append(endYear);
            }
        }
    }

    @Override
    public void parseUrl(String url) {
        //todo parse Url
        setTitle(URLDecoder.decode(URLDecoder.decode(parseUrlItem(url, "title"))).replace("+++8", ""));
//        setQkname(getTitle());
        setStartYear(parseUrlItem(url, "start_year"));
        setEndYear(parseUrlItem(url, "end_year"));
        setPagenow(Integer.parseInt(parseUrlItem(url, "pagenow")));
        setPageSize(Integer.parseInt(parseUrlItem(url, "pagesize")));
        setOrderPx(parseUrlItem(url, "order_px"));


    }

    public String getQkname() {
        return qkname;
    }

    public void setQkname(String qkname) {
        this.qkname = qkname;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderPx() {
        return orderPx;
    }

    public void setOrderPx(String orderPx) {
        this.orderPx = orderPx;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPagenow() {
        return pagenow;
    }

    public void setPagenow(int pagenow) {
        this.pagenow = pagenow;
    }

    public String getXkfl() {
        return xkfl;
    }

    public void setXkfl(String xkfl) {
        this.xkfl = xkfl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public CssciPaperParam clone() {
        CssciPaperParam param = null;
        try {
            param = (CssciPaperParam) super.clone();
        } catch (CloneNotSupportedException e) {
            LogRecord.error("CloneNotSupportedException");
        }
        BeanUtils.copyProperties(this, param);
        return param;
    }

    @Override
    public String toString() {
        return "CssciPaperParam{" +
                "qkname='" + qkname + '\'' +
                ", title='" + title + '\'' +
                ", startYear='" + startYear + '\'' +
                ", endYear='" + endYear + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderPx='" + orderPx + '\'' +
                ", pageSize=" + pageSize +
                ", pagenow=" + pagenow +
                '}';
    }
}

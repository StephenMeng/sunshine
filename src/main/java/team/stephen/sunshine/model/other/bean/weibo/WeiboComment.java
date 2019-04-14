package team.stephen.sunshine.model.other.bean.weibo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/30 02:30
 */
@Entity
@Table(name = "weibo_comment")
public class WeiboComment {
    @Column(name = "w_mid")
    private String wMid;
    @Column(name = "w_ouid")
    private String wOuid;
    @Column(name = "c_user_name")
    private String cUserName;
    @Column(name = "c_date")
    private String cDate;
    @Column(name = "c_content")
    private String cContent;
    @Column(name = "c_user_url")
    private String cUserUrl;
    @Column(name = "c_reply")
    private String cReply;
    @Column(name = "c_thumb")
    private String cThumb;
    @Column(name = "c_crawl_date")
    private Date cCrawlDate;

    public String getwMid() {
        return wMid;
    }

    public void setwMid(String wMid) {
        this.wMid = wMid;
    }

    public String getwOuid() {
        return wOuid;
    }

    public void setwOuid(String wOuid) {
        this.wOuid = wOuid;
    }

    public String getcUserName() {
        return cUserName;
    }

    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public String getcUserUrl() {
        return cUserUrl;
    }

    public void setcUserUrl(String cUserUrl) {
        this.cUserUrl = cUserUrl;
    }

    public String getcReply() {
        return cReply;
    }

    public void setcReply(String cReply) {
        this.cReply = cReply;
    }

    public String getcThumb() {
        return cThumb;
    }

    public void setcThumb(String cThumb) {
        this.cThumb = cThumb;
    }

    public Date getcCrawlDate() {
        return cCrawlDate;
    }

    public void setcCrawlDate(Date cCrawlDate) {
        this.cCrawlDate = cCrawlDate;
    }
}

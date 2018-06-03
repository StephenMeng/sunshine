package team.stephen.sunshine.model.other;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/30 01:12
 */
@Entity
@Table(name = "weibo")
public class Weibo {
    @Id
    @Column(name = "w_url")
    private String wUrl;
    @Column(name = "w_ouid")
    private String wOuid;
    @Column(name = "w_mid")
    private String wMid;
    @Column(name = "w_user_name")
    private String wUserName;
    @Column(name = "w_date")
    private String wDate;
    @Column(name = "w_from")
    private String wFrom;
    @Column(name = "w_share_count")
    private String wShareCount;
    @Column(name = "w_comment_count")
    private String wCommentCount;
    @Column(name = "w_thumb_count")
    private String wThumbCount;
    @Column(name = "w_content")
    private String wContent;
    @Column(name = "w_pics")
    private String wPics;
    @Column(name = "crawl_date")
    private Date crawlDate;
    public String getwUrl() {
        return wUrl;
    }

    public void setwUrl(String wUrl) {
        this.wUrl = wUrl;
    }

    public String getwOuid() {
        return wOuid;
    }

    public void setwOuid(String wOuid) {
        this.wOuid = wOuid;
    }

    public String getwMid() {
        return wMid;
    }

    public void setwMid(String wMid) {
        this.wMid = wMid;
    }

    public String getwUserName() {
        return wUserName;
    }

    public void setwUserName(String wUserName) {
        this.wUserName = wUserName;
    }

    public String getwDate() {
        return wDate;
    }

    public void setwDate(String wDate) {
        this.wDate = wDate;
    }

    public String getwFrom() {
        return wFrom;
    }

    public void setwFrom(String wFrom) {
        this.wFrom = wFrom;
    }

    public String getwShareCount() {
        return wShareCount;
    }

    public void setwShareCount(String wShareCount) {
        this.wShareCount = wShareCount;
    }

    public String getwCommentCount() {
        return wCommentCount;
    }

    public void setwCommentCount(String wCommentCount) {
        this.wCommentCount = wCommentCount;
    }

    public String getwThumbCount() {
        return wThumbCount;
    }

    public void setwThumbCount(String wThumbCount) {
        this.wThumbCount = wThumbCount;
    }

    public String getwContent() {
        return wContent;
    }

    public void setwContent(String wContent) {
        this.wContent = wContent;
    }

    public String getwPics() {
        return wPics;
    }

    public void setwPics(String wPics) {
        this.wPics = wPics;
    }

    public Date getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Date crawlDate) {
        this.crawlDate = crawlDate;
    }
}

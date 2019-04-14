package team.stephen.sunshine.model.other.bean.weibo;

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
    @Column(name = "w_user_url")
    private String wUserUrl;
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
    @Column(name = "w_collect_count")
    private String wCollectCount;
    @Column(name = "w_content")
    private String wContent;
    @Column(name = "w_pics")
    private String wPics;
    @Column(name = "qid")
    private String qid;
    @Column(name = "full_content_param")
    private String fullContentParam;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;

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

    public String getwUserUrl() {
        return wUserUrl;
    }

    public void setwUserUrl(String wUserUrl) {
        this.wUserUrl = wUserUrl;
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

    public String getwCollectCount() {
        return wCollectCount;
    }

    public void setwCollectCount(String wCollectCount) {
        this.wCollectCount = wCollectCount;
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

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getFullContentParam() {
        return fullContentParam;
    }

    public void setFullContentParam(String fullContentParam) {
        this.fullContentParam = fullContentParam;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "Weibo{" +
                "wUrl='" + wUrl + '\'' +
                ", wOuid='" + wOuid + '\'' +
                ", wMid='" + wMid + '\'' +
                ", wUserName='" + wUserName + '\'' +
                ", wUserUrl='" + wUserUrl + '\'' +
                ", wDate='" + wDate + '\'' +
                ", wFrom='" + wFrom + '\'' +
                ", wShareCount='" + wShareCount + '\'' +
                ", wCommentCount='" + wCommentCount + '\'' +
                ", wThumbCount='" + wThumbCount + '\'' +
                ", wCollectCount='" + wCollectCount + '\'' +
                ", wContent='" + wContent + '\'' +
                ", wPics='" + wPics + '\'' +
                ", qid='" + qid + '\'' +
                ", fullContentParam='" + fullContentParam + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}

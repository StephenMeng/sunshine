package team.stephen.sunshine.model.other.bean.weibo;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/06/02 23:01
 */
@Entity
@Table(name = "weibo_user_config")
public class WeiboUserConfig {
    @Id
    @Column(name = "oid")
    private String oid;
    @Column(name = "page_id")
    private String pageId;
    @Column(name = "uri")
    private String uri;
    @Column(name = "name")
    private String name;
    @Column(name = "domain")
    private String domain;
    @Column(name = "pids")
    private String pids;
    @Column(name = "weibo_num")
    private Integer weiboNum;
    @Column(name = "fans_num")
    private Integer fansNum;
    @Column(name = "follow_num")
    private Integer followNum;
    @Column(name = "lv")
    private String lv;
    @Column(name = "place")
    private String place;
    @Column(name = "description")
    private String description;
    @Column(name = "link")
    private String link;
    @Column(name = "tag")
    private String tag;
    @Column(name = "baike")
    private String baike;
    @Column(name = "birthday")
    private String birthday;
    @Column(name = "career")
    private String career;
    @Column(name = "pinfo")
    private String pinfo;
    @Column(name = "sex")
    private String sex;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "keyword")
    private String keyword;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public Integer getWeiboNum() {
        return weiboNum;
    }

    public void setWeiboNum(Integer weiboNum) {
        this.weiboNum = weiboNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPinfo() {
        return pinfo;
    }

    public void setPinfo(String pinfo) {
        this.pinfo = pinfo;
    }

    public String getBaike() {
        return baike;
    }

    public void setBaike(String baike) {
        this.baike = baike;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "WeiboUserConfig{" +
                "oid='" + oid + '\'' +
                ", pageId='" + pageId + '\'' +
                ", uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", pids='" + pids + '\'' +
                ", weiboNum=" + weiboNum +
                ", fansNum=" + fansNum +
                ", followNum=" + followNum +
                ", lv='" + lv + '\'' +
                ", place='" + place + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", tag='" + tag + '\'' +
                ", baike='" + baike + '\'' +
                ", birthday='" + birthday + '\'' +
                ", career='" + career + '\'' +
                ", pinfo='" + pinfo + '\'' +
                ", sex='" + sex + '\'' +
                ", updateDate=" + updateDate +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    public void setlv(String lv) {
        this.lv = lv;
    }
}

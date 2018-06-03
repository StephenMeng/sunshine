package team.stephen.sunshine.model.other;

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

    @Column(name = "oid")
    private String oid;
    @Id
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
    @Column(name = "update_date")
    private Date updateDate;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

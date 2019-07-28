package team.stephen.sunshine.model.other.bean.weibo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/06/02 23:01
 */
@Data
@ToString
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
}

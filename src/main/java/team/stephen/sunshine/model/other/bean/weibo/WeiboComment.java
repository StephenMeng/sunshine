package team.stephen.sunshine.model.other.bean.weibo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/30 02:30
 */
@Data
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
}

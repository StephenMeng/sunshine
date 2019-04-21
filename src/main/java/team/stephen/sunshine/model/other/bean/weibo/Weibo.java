package team.stephen.sunshine.model.other.bean.weibo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/30 01:12
 */
@Data
@Entity
@NoArgsConstructor
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
}

package team.stephen.sunshine.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 附件实体类
 *
 * @author stephen
 * @date 2018/5/22
 */
@Entity
@Table(name = "sunshine_article_attach")
public class ArticleAttachRelation {
    @Id
    @Column(name = "article_id")
    private Long articleId;
    @Id
    @Column(name = "attach_uri")
    private String attachUri;
    @Column(name = "attach_name")
    private String attachName;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getAttachUri() {
        return attachUri;
    }

    public void setAttachUri(String attachUri) {
        this.attachUri = attachUri;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }
}
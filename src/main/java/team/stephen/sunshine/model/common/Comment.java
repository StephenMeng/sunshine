package team.stephen.sunshine.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/26 15:10
 */
@Entity
@Table(name = "sunshine_comment")
public class Comment {
    @Id
    @Column(name = "comment_id")
    private Long commentId;
    @Column(name = "comment_user_id")
    private Integer commentUserId;
    @Column(name = "comment_on_bin_id")
    private Long commentOnBinId;
    @Column(name = "comment_on_bin_type")
    private Integer commentOnBinType;
    @Column(name = "comment_content")
    private String commentContent;
    @Column(name = "comment_date")
    private Date commentDate;
    @Column(name = "deleted")
    private Boolean deleted;

    public static Comment getNewDefaultInstance() {
        Comment comment=new Comment();
        comment.setDeleted(false);
        comment.setCommentDate(new Date());
        return comment;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    public Long getCommentOnBinId() {
        return commentOnBinId;
    }

    public void setCommentOnBinId(Long commentOnBinId) {
        this.commentOnBinId = commentOnBinId;
    }

    public Integer getCommentOnBinType() {
        return commentOnBinType;
    }

    public void setCommentOnBinType(Integer commentOnBinType) {
        this.commentOnBinType = commentOnBinType;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

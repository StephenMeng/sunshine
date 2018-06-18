package team.stephen.sunshine.web.dto.comment;

import team.stephen.sunshine.web.dto.user.UserDto;

import java.util.Date;

/**
 * @author Stephen
 * @date 2018/06/17 15:12
 */
public class CommentDto {
    private Long commentId;
    private UserDto commentUser;
    private Long commentOnBinId;
    private Date commentDate;
    private String commentContent;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public UserDto getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(UserDto commentUser) {
        this.commentUser = commentUser;
    }

    public Long getCommentOnBinId() {
        return commentOnBinId;
    }

    public void setCommentOnBinId(Long commentOnBinId) {
        this.commentOnBinId = commentOnBinId;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}

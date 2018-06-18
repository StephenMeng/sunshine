package team.stephen.sunshine.web.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Stephen
 * @date 2018/06/17 15:12
 */
@ApiModel(description = "comment 输入实体类")
public class CommentInputDto {
    @ApiModelProperty(value = "文章linkId")
    private String articleLinkId;
    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    public String getArticleLinkId() {
        return articleLinkId;
    }

    public void setArticleLinkId(String articleLinkId) {
        this.articleLinkId = articleLinkId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}

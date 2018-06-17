package team.stephen.sunshine.web.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;
import team.stephen.sunshine.web.dto.common.AttachmentDto;

import java.util.List;

/**
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "article 输入实体类")
public class InputArticleDto extends BaseArticleDto {
    @ApiModelProperty(value = "正文")
    private String articleContent;
    @ApiModelProperty(value = "频道ID")
    private String channel;
    @ApiModelProperty(value = "栏目ID")
    private String column;
    @ApiModelProperty(value = "是否发布")
    private Boolean articleIsPublished;
    @ApiModelProperty(value = "是否私有")
    private Boolean isPrivate;
    @ApiModelProperty(value = "标签")
    private List<String>tags;
    @ApiModelProperty(value = "附件")
    private List<AttachmentDto>attachments;
    @ApiModelProperty
    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Boolean getArticleIsPublished() {
        return articleIsPublished;
    }

    public void setArticleIsPublished(Boolean articleIsPublished) {
        this.articleIsPublished = articleIsPublished;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

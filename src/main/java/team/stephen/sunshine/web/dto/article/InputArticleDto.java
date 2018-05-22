package team.stephen.sunshine.web.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;

/**
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "article 输入实体类")
public class InputArticleDto extends BaseArticleDto {
    @ApiModelProperty(value = "正文")
    private String articleContent;
    @ApiModelProperty(value = "频道ID")
    private Integer channelId;
    @ApiModelProperty(value = "栏目ID")
    private Integer columnId;
    @ApiModelProperty(value = "是否发布")
    private Boolean articleIsPublished;
    @ApiModelProperty(value = "是否私有")
    private Boolean isPrivate;

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
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
}

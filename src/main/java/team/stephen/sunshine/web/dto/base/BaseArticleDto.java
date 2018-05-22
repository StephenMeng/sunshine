package team.stephen.sunshine.web.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.model.article.Article;

import java.util.Date;

/**
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "article 实体类")
public class BaseArticleDto {
    @ApiModelProperty(value = "题名")
    protected String articleTitle;
    @ApiModelProperty(value = "标签")
    protected String articleTag;
    @ApiModelProperty(value = "作者")
    protected Integer articleAuthor;
    @ApiModelProperty(value = "链接")
    protected String articleLink;
    @ApiModelProperty(value = "创建日期")
    protected Date articleCreateDate;
    @ApiModelProperty(value = "linkId")
    protected String articleLinkId;

    public BaseArticleDto() {
    }

    public BaseArticleDto(Article source) {
        setArticleAuthor(source.getArticleAuthor());
        setArticleCreateDate(source.getArticleCreateDate());
        setArticleLink(source.getArticleLink());
        setArticleTag(source.getArticleTag());
        setArticleTitle(source.getArticleTitle());
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public Integer getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(Integer articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Date getArticleCreateDate() {
        return articleCreateDate;
    }

    public void setArticleCreateDate(Date articleCreateDate) {
        this.articleCreateDate = articleCreateDate;
    }

    public String getArticleLinkId() {
        return articleLinkId;
    }

    public void setArticleLinkId(String articleLinkId) {
        this.articleLinkId = articleLinkId;
    }
}

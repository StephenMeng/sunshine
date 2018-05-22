package team.stephen.sunshine.web.dto.base;

import team.stephen.sunshine.model.article.Article;

import java.util.Date;

/**
 * @author stephen
 * @date 2018/5/22
 */
public class BaseArticleDto {
    protected String articleLinkId;
    protected String articleTitle;
    protected String articleTag;
    protected Integer articleAuthor;
    protected String articleLink;
    protected Date articleCreateDate;

    public BaseArticleDto() {
    }

    public BaseArticleDto(Article source) {
        setArticleAuthor(source.getArticleAuthor());
        setArticleCreateDate(source.getArticleCreateDate());
        setArticleLink(source.getArticleLink());
        setArticleLinkId(source.getArticleLinkId());
        setArticleTag(source.getArticleTag());
        setArticleTitle(source.getArticleTitle());
    }

    public String getArticleLinkId() {
        return articleLinkId;
    }

    public void setArticleLinkId(String articleLinkId) {
        this.articleLinkId = articleLinkId;
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
}

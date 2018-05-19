package team.stephen.sunshine.model.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author stephen meng
 * @date 2018/5/19
 */
@Entity
@Table(name = "sunshine_article")
public class Article {
    @Id
    @Column(name = "article_id")
    private Long articleId;
    @Column(name = "article_link_id")
    private String articleLinkId;
    @Column(name = "article_title")
    private String articleTitle;
    @Column(name = "article_abstract")
    private String articleAbstract;
    @Column(name = "article_tag")
    private String articleTag;
    @Column(name = "article_author")
    private Integer articleAuthor;
    @Column(name = "article_comment_count")
    private Integer articleCommentCount;
    @Column(name = "article_view_count")
    private Integer articleViewCount;
    @Column(name = "article_content")
    private String articleContent;
    @Column(name = "article_link")
    private String articleLink;
    @Column(name = "article_had_been_published")
    private Boolean articleHadBeenPublished;
    @Column(name = "article_is_published")
    private Boolean articleIsPublished;
    @Column(name = "article_put_top")
    private Boolean articlePutTop;
    @Column(name = "article_create_date")
    private Date articleCreateDate;
    @Column(name = "article_update_date")
    private Date articleUpdateDate;
    @Column(name = "article_view_pwd")
    private String articleViewPwd;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
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

    public Integer getArticleCommentCount() {
        return articleCommentCount;
    }

    public void setArticleCommentCount(Integer articleCommentCount) {
        this.articleCommentCount = articleCommentCount;
    }

    public Integer getArticleViewCount() {
        return articleViewCount;
    }

    public void setArticleViewCount(Integer articleViewCount) {
        this.articleViewCount = articleViewCount;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Boolean getArticleHadBeenPublished() {
        return articleHadBeenPublished;
    }

    public void setArticleHadBeenPublished(Boolean articleHadBeenPublished) {
        this.articleHadBeenPublished = articleHadBeenPublished;
    }

    public Boolean getArticleIsPublished() {
        return articleIsPublished;
    }

    public void setArticleIsPublished(Boolean articleIsPublished) {
        this.articleIsPublished = articleIsPublished;
    }

    public Boolean getArticlePutTop() {
        return articlePutTop;
    }

    public void setArticlePutTop(Boolean articlePutTop) {
        this.articlePutTop = articlePutTop;
    }

    public Date getArticleCreateDate() {
        return articleCreateDate;
    }

    public void setArticleCreateDate(Date articleCreateDate) {
        this.articleCreateDate = articleCreateDate;
    }

    public Date getArticleUpdateDate() {
        return articleUpdateDate;
    }

    public void setArticleUpdateDate(Date articleUpdateDate) {
        this.articleUpdateDate = articleUpdateDate;
    }

    public String getArticleViewPwd() {
        return articleViewPwd;
    }

    public void setArticleViewPwd(String articleViewPwd) {
        this.articleViewPwd = articleViewPwd;
    }
}
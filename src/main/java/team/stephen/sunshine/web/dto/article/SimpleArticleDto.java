package team.stephen.sunshine.web.dto.article;

import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;
import team.stephen.sunshine.web.dto.user.UserDto;

import java.util.Date;

/**
 * @author stephen
 * @date //
 */
public class SimpleArticleDto extends BaseArticleDto {
    private Long articleId;
    private Integer channelId;
    private Integer columnId;
    private String articleAbstract;
    private Integer articleCommentCount;
    private Integer articleViewCount;
    private Boolean articleHadBeenPublished;
    private Boolean articleIsPublished;
    private Boolean articlePutTop;
    private Date articleUpdateDate;
    private String articleViewPwd;
    private UserDto articleAuthor;
    public SimpleArticleDto() {
        super();
    }

    public SimpleArticleDto(Article source) {
        super(source);
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
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

    public UserDto getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(UserDto articleAuthor) {
        this.articleAuthor = articleAuthor;
    }
}

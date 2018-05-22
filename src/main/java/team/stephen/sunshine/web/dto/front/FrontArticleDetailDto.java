package team.stephen.sunshine.web.dto.front;

import team.stephen.sunshine.web.dto.base.BaseArticleDto;

import java.util.Date;

/**
 * @author stephen
 * @date 2018/5/22
 */
public class FrontArticleDetailDto extends BaseArticleDto {
    private String channelUri;
    private String columnUri;
    private String articleAbstract;
    private Integer articleCommentCount;
    private Integer articleViewCount;
    private Boolean articleHadBeenPublished;
    private Boolean articleIsPublished;
    private Boolean articlePutTop;
    private Date articleUpdateDate;
    private String articleViewPwd;

    public String getChannelUri() {
        return channelUri;
    }

    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

    public String getColumnUri() {
        return columnUri;
    }

    public void setColumnUri(String columnUri) {
        this.columnUri = columnUri;
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
}

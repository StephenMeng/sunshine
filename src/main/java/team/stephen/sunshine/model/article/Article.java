package team.stephen.sunshine.model.article;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import team.stephen.sunshine.util.common.RandomIDUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author stephen meng
 * @date 2018/5/19
 */
@Data
@Entity
@Table(name = "sunshine_article")
public class Article {
    @Id
    @Column(name = "article_id")
    @Field("articleId")
    private Long articleId;
    @Column(name = "article_link_id")
    private String articleLinkId;
    @Column(name = "channel_id")
    private Integer channelId;
    @Column(name = "column_id")
    private Integer columnId;
    @Column(name = "article_title")
    private String articleTitle;
    @Column(name = "article_abstract")
    private String articleAbstract;
    @Column(name = "article_tag")
    @Field("articleTag")
    private String articleTag;
    @Column(name = "article_author")
    private Integer articleAuthorId;
    @Column(name = "article_comment_count")
    private Integer articleCommentCount;
    @Column(name = "article_view_count")
    private Integer articleViewCount;
    @Column(name = "article_content")
    @Field("articleContent")
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
    @Column(name = "private")
    private Boolean isPrivate;
    @Column(name = "deleted")
    private Boolean deleted;

    public static Article getNewDefaultInstance() {
        Article article = new Article();
        article.setArticleLinkId(RandomIDUtil.randomID(16));
        article.setArticleUpdateDate(new Date());
        article.setArticleCreateDate(new Date());
        article.setArticleCommentCount(0);
        article.setArticleHadBeenPublished(false);
        article.setArticleIsPublished(false);
        article.setArticleViewCount(0);
        article.setArticlePutTop(false);
        article.setDeleted(false);
        article.setIsPrivate(false);
        return article;
    }
}

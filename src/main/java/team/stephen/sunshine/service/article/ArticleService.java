package team.stephen.sunshine.service.article;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.common.Attachment;
import team.stephen.sunshine.web.dto.common.AttachmentDto;

import java.util.List;

public interface ArticleService {
    Article selectArticleById(Long articleId);

    Article selectArticleByLinkId(String linkId);

    int addArticle(Article article) throws Exception;

    int updateSelective(Article article);

    Page<Article> select(Article articleCondition, Integer pageNum, Integer pageSize);

    Article selectOne(Article articleCondition);

    List<AttachmentDto>getAttachments(Long articleId);

    int addAttachments(Long articleId,List<AttachmentDto>uri);
}

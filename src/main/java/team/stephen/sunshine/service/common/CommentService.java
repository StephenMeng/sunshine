package team.stephen.sunshine.service.common;

import com.github.pagehelper.Page;
import io.swagger.models.auth.In;
import team.stephen.sunshine.model.common.Comment;
import team.stephen.sunshine.util.common.ParamCheck;

/**
 * @author Stephen
 * @date 2018/05/26 15:14
 */
public interface CommentService {
    Page<Comment> selectArticleOnArticle(Long articleId, Integer pageNum, Integer pageSize);

    Page<Comment> selectCommentOnComment(Long articleId, Integer pageNum, Integer pageSize);

    ParamCheck addCommentOnArticle(Comment comment);

    ParamCheck addCommentOnComment(Comment comment);

    ParamCheck deleteComment(Long commentId);
}

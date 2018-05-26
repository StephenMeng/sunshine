package team.stephen.sunshine.service.common.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.common.CommentDao;
import team.stephen.sunshine.model.common.Comment;
import team.stephen.sunshine.service.common.CommentService;
import team.stephen.sunshine.util.common.ParamCheck;

import static team.stephen.sunshine.constant.CommentType.COMMENT_ON_ARTICLE;
import static team.stephen.sunshine.constant.CommentType.COMMENT_ON_COMMENT;

/**
 * @author Stephen
 * @date 2018/05/26 15:14
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public Page<Comment> selectArticleOnArticle(Long articleId, Integer pageNum, Integer pageSize) {
        return getComments(articleId, pageNum, pageSize, COMMENT_ON_ARTICLE);
    }

    @Override
    public Page<Comment> selectCommentOnComment(Long articleId, Integer pageNum, Integer pageSize) {
        return getComments(articleId, pageNum, pageSize, COMMENT_ON_COMMENT);
    }

    private Page<Comment> getComments(Long articleId, Integer pageNum, Integer pageSize, int commentOnArticle) {
        Comment condition = new Comment();
        condition.setCommentOnBinId(articleId);
        condition.setCommentOnBinType(commentOnArticle);
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Comment>) commentDao.select(condition);
    }

    @Override
    public ParamCheck addCommentOnArticle(Comment comment) {
        comment.setCommentOnBinType(COMMENT_ON_ARTICLE);
        commentDao.insert(comment);
        return ParamCheck.right();
    }

    @Override
    public ParamCheck addCommentOnComment(Comment comment) {
        comment.setCommentOnBinType(COMMENT_ON_COMMENT);
        commentDao.insert(comment);
        return ParamCheck.right();
    }

    @Override
    public ParamCheck deleteComment(Long commentId) {
        Comment condition = new Comment();
        condition.setCommentId(commentId);
        condition.setDeleted(true);
        commentDao.updateByPrimaryKeySelective(condition);
        return ParamCheck.right();
    }
}
package team.stephen.sunshine.dao.common;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.common.Comment;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author Stephen
 * @date 2018/05/26 15:05
 */
@Mapper
public interface CommentDao extends BaseDao<Comment> {
}

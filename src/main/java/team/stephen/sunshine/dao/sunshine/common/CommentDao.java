package team.stephen.sunshine.dao.sunshine.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.stephen.sunshine.model.common.Comment;
import team.stephen.sunshine.util.common.BaseDao;

import java.util.List;

/**
 * @author Stephen
 * @date 2018/05/26 15:05
 */
@Mapper
public interface CommentDao extends BaseDao<Comment> {
    List<Comment> selectByCondition(@Param("comment") Comment comment);
}

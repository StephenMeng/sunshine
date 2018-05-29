package team.stephen.sunshine.dao.other;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author Stephen
 * @date 2018/05/30 02:32
 */
@Mapper
public interface WeiboCommentDao extends BaseDao<WeiboComment>{
}

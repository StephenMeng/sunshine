package team.stephen.sunshine.dao.sunshine.permission;


import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.user.UserRoleRelation;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * 用户和用户角色关系dao
 *
 * @author stephen
 * @date 2018/5/19
 */
@Mapper
public interface UserRoleRelationDao extends BaseDao<UserRoleRelation> {
}

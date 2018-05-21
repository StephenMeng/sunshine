package team.stephen.sunshine.dao.permission;


import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.common.RolePermissionRelation;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author stephen
 * @date 2017/7/15
 */
@Mapper
public interface RolePermissionRelationDao extends BaseDao<RolePermissionRelation> {
}

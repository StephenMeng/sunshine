package team.stephen.sunshine.dao.user;


import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.util.BaseDao;

/**
 * @author stephen
 * @date 2017/7/15
 */
@Mapper
public interface UserDao extends BaseDao<User> {
}

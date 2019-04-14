package team.stephen.sunshine.dao.other;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.other.bean.weibo.WeiboUserConfig;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author Stephen
 * @date 2018/06/02 23:02
 */
@Mapper
public interface WeiboUserConfigDao extends BaseDao<WeiboUserConfig> {
}

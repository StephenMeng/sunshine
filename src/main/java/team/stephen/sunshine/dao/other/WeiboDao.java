package team.stephen.sunshine.dao.other;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.other.bean.weibo.Weibo;
import team.stephen.sunshine.util.common.BaseDao;

import java.util.List;

/**
 * @author Stephen
 * @date 2018/05/30 02:32
 */
@Mapper
public interface WeiboDao extends BaseDao<Weibo> {
    List<String> selectAllUserIdsFromWeibo();

    List<String> selectAllUrlsFromWeibo();

}

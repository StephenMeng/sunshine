package team.stephen.sunshine.dao.other;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author Stephen
 * @date 2018/06/03 09:46
 */
@Mapper
public interface CrawlErrorDao extends BaseDao<CrawlError> {
}

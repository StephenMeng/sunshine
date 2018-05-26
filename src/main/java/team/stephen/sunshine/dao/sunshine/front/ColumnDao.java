package team.stephen.sunshine.dao.sunshine.front;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author Stephen
 * @date 2018/05/21 23:20
 */
@Mapper
public interface ColumnDao extends BaseDao<Column> {
}

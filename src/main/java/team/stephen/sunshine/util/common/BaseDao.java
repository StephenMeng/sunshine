package team.stephen.sunshine.util.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author stephen
 * @date 2017/7/15
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T> {
}

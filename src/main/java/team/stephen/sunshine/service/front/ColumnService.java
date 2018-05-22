package team.stephen.sunshine.service.front;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;

/**
 * @author Stephen
 * @date 2018/05/21 23:22
 */
public interface ColumnService {
    Column selectBycolumnId(int id);

    Page<Column> select(Column condition, int pageNum, int pageSize);

    int addcolumn(Column column);

    int updateSelective(Column column);

    int delete(int columnId);

    int restore(int columnId);

    Column selectOne(Column condition);
}

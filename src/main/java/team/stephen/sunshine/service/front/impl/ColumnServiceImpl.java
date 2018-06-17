package team.stephen.sunshine.service.front.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.sunshine.front.ColumnDao;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.service.front.ColumnService;

/**
 * @author Stephen
 * @date 2018/05/21 23:23
 */
@Service
public class ColumnServiceImpl implements ColumnService {
    @Autowired
    private ColumnDao columnDao;

    @Override
    public Column selectBycolumnId(int id) {
        return columnDao.selectByPrimaryKey(id);
    }

    @Override
    public Page<Column> select(Column condition, int pageNum, int pageSize) {
        if (condition == null) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Column>) columnDao.select(condition);
    }

    @Override
    public int addcolumn(Column column) {
        return columnDao.insert(column);
    }

    @Override
    public int updateSelective(Column column) {
        return columnDao.updateByPrimaryKeySelective(column);
    }

    @Override
    public int delete(int columnId) {
        Column column = new Column();
        column.setColumnId(columnId);
        column.setDeleted(true);
        return updateSelective(column);
    }

    @Override
    public int restore(int columnId) {
        Column column = new Column();
        column.setColumnId(columnId);
        column.setDeleted(false);
        return updateSelective(column);
    }

    @Override
    public Column selectOne(Column condition) {
        return columnDao.selectOne(condition);
    }

    @Override
    public Column selectBycolumnUri(String column) {
        Column condition = new Column();
        condition.setColumnUri(column);
        return columnDao.selectOne(condition);
    }
}

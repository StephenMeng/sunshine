package team.stephen.sunshine.dao.sunshine.book;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.util.common.BaseDao;

@Mapper
public interface BookDao extends BaseDao<Book> {
}

package team.stephen.sunshine.dao.book;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.util.BaseDao;

@Mapper
public interface BookDao extends BaseDao<Book> {
}
package team.stephen.sunshine.service.book.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.book.BookDao;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.service.book.BookService;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;

    @Override
    public int addBook(Book book) {
        try {
            return bookDao.insert(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}

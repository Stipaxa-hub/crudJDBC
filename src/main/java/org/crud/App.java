package org.crud;

import org.crud.dao.BookDao;
import org.crud.dao.impl.BookDaoImpl;
import org.crud.model.Book;

import java.math.BigDecimal;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        BookDao bookDao = new BookDaoImpl();
        bookDao.create(Book.builder()
                        .title("title")
                        .price(BigDecimal.valueOf(20))
                .build());
    }
}

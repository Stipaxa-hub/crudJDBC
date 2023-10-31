package org.crud.dao;

import org.crud.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book create(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();

    Book update(Book book, Long id);

    boolean delete(Book book);
}

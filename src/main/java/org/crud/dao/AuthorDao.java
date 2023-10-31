package org.crud.dao;

import org.crud.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    Author create(Author author);

    Optional<Author> findById(int id);

    List<Author> findAll();

    Author update(Author author, int id);

    boolean delete(Author author);
}

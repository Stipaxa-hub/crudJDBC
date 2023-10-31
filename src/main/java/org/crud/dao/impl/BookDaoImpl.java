package org.crud.dao.impl;

import org.crud.dao.BookDao;
import org.crud.dao.exception.DataProcessingException;
import org.crud.model.Book;
import org.crud.util.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
    @Override
    public Book create(Book book) {
        String query = "INSERT INTO book (title, price) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getObject(1, Integer.class);
                book.setId(id);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add a new book", e);
        }
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String query = "SELECT * FROM book WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = getBookFromResultSet(resultSet);
                return Optional.of(book);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find book with id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        String query = "SELECT * FROM book";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Book book = getBookFromResultSet(resultSet);
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find any books!",e);
        }
    }

    @Override
    public Book update(Book book, Long id) {
        String query = "UPDATE book SET title = ?, price = ? WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            statement.setLong(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update book with id " + book.getId(), e);
        }
        return book;
    }

    @Override
    public boolean delete(Book book) {
        String query = "DELETE FROM book WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, book.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Error deleting book " + book.getId(), e);
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = Book.builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .price(resultSet.getBigDecimal("price"))
                .build();
        return book;
    }
}

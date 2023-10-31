package org.crud.dao.impl;

import org.crud.dao.AuthorDao;
import org.crud.dao.exception.DataProcessingException;
import org.crud.model.Author;
import org.crud.model.Book;
import org.crud.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl implements AuthorDao {
    @Override
    public Author create(Author author) {
        String query = "INSERT INTO author ( last_name, first_name, age, book_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, author.getLastName());
            statement.setString(2, author.getFirstName());
            statement.setInt(3, author.getAge());
            statement.setInt(4, author.getBookId());
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getObject(1, Integer.class);
                author.setId(id);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add a new author", e);
        }
        return author;
    }

    @Override
    public Optional<Author> findById(int id) {
        String query = "SELECT * FROM author WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Author author = getAuthorFromResultSet(resultSet);
                return Optional.of(author);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find author with id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Author> findAll() {
        String query = "SELECT * FROM author";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                Author author = getAuthorFromResultSet(resultSet);
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find any author!",e);
        }
    }

    @Override
    public Author update(Author author, int id) {
        String query = "UPDATE author SET last_name = ?, first_name = ?, age = ?, book_id = ? WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, author.getLastName());
            statement.setString(2, author.getFirstName());
            statement.setInt(3, author.getAge());
            statement.setInt(4, author.getBookId());
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update author with id " + author.getId(), e);
        }
        return author;
    }

    @Override
    public boolean delete(Author author) {
        String query = "DELETE FROM author WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, author.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Error deleting author " + author.getId(), e);
        }
    }

    private Author getAuthorFromResultSet(ResultSet resultSet) throws SQLException {
        Author author = Author.builder()
                .id(resultSet.getInt("id"))
                .lastName(resultSet.getString("last_name"))
                .firstName(resultSet.getString("first_name"))
                .age(resultSet.getInt("age"))
                .bookId(resultSet.getInt("book_id"))
                .build();
        return author;
    }
}

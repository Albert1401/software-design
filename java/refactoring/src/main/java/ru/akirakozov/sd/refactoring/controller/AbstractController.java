package ru.akirakozov.sd.refactoring.controller;

import java.sql.*;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractController<E, I> {

    public abstract void createEntity(E e);

    public abstract List<E> getAll();

    public abstract E getById(I id);

    public abstract void deleteEntity(E e);


    protected <R> R executeSelectQuery(String query, Function<ResultSet, R> f) {
        PreparedStatement st = null;
        try {
            st = getPreparedStatement(query);
            ResultSet rs = st.executeQuery();
            return f.apply(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closePreparedStatement(st);
        }
    }

    protected void executeUpdateQuery(String query) {
        PreparedStatement st = null;
        try {
            st = getPreparedStatement(query);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closePreparedStatement(st);
        }
    }

    protected PreparedStatement getPreparedStatement(String query) {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
            return c.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closePreparedStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

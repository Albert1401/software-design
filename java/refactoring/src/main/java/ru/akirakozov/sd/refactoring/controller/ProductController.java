package ru.akirakozov.sd.refactoring.controller;

import ru.akirakozov.sd.refactoring.functional.CheckedFunction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductController extends AbstractController<Product, Integer> {

    private static final String insertQuery = "INSERT INTO PRODUCT (NAME, PRICE)" +
            " VALUES ('%s', '%d')";

    private static final String selectAllQuery = "SELECT * FROM PRODUCT";
    private static final String selectByIdQuery = "SELECT * FROM PRODUCT WHERE ID = %d";
    private static final String selectCountQuery = "SELECT COUNT(*) FROM PRODUCT";
    private static final String selectSumQuery = "SELECT SUM(price) FROM PRODUCT";
    private static final String selectMinQuery = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    private static final String selectMaxQuery = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";

    public ProductController() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createEntity(Product product) {
        String q = String.format(insertQuery, product.getName(), product.getPrice());
        executeUpdateQuery(q);
    }

    @Override
    public List<Product> getAll() {
        return executeSelectQuery(selectAllQuery, rs -> {
            try {
                List<Product> products = new ArrayList<>(rs.getFetchSize());
                while (rs.next()) {
                    Product p = new Product(
                            rs.getString("name"),
                            rs.getLong("price")
                    );
                    products.add(p);
                }
                return products;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Product getById(Integer id) {
        String q = String.format(selectByIdQuery, id);
        return executeAggregate(q, rs -> new Product(rs.getString("name"), rs.getLong("price")));
    }

    @Override
    public void deleteEntity(Product product) {
        throw new NotImplementedException();
    }

    public int getCount() {
        return executeAggregate(selectCountQuery, rs -> rs.getInt(1));
    }

    public int getSum() {
        return executeAggregate(selectSumQuery, rs -> rs.getInt(1));
    }

    public Product getMin() {
        return executeAggregate(selectMinQuery, rs -> new Product(rs.getString("name"), rs.getLong("price")));
    }

    public Product getMax() {
        return executeAggregate(selectMaxQuery, rs -> new Product(rs.getString("name"), rs.getLong("price")));
    }

    private <R> R executeAggregate(String query, CheckedFunction<ResultSet, R> firstRow) {
        return executeSelectQuery(query, rs -> {
            try {
                rs.next();
                return firstRow.apply(rs);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

package ru.mail.track.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Обертка для запроса в базу
 * <p>
 * Также можно инкапсулировать Connection внутрь
 */
public class QueryExecutor {

    Connection connection;

    private Map<String, PreparedStatement> execPrepareds = new HashMap<>();
    private Map<String, PreparedStatement> updatePrepareds = new HashMap<>();
    private Map<String, PreparedStatement> updateWithKeysPrepareds = new HashMap<>();

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // Простой запрос
    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();

        return value;
    }

    // Подготовленный запрос
    public <T> T execQuery(String query, Map<Integer, Object> args, ResultHandler<T> handler) throws SQLException {

        PreparedStatement stmt = null;
        if (execPrepareds.containsKey(query)) {
            stmt = execPrepareds.get(query);
        } else {
            stmt = connection.prepareStatement(query);
            execPrepareds.put(query, stmt);
        }

        stmt.clearParameters();

        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }
        ResultSet rs = stmt.executeQuery();
        T value = handler.handle(rs);
        rs.close();
        //stmt.close();
        return value;
    }

    // Также нужно реализовать Update запросы
    public void updateQuery(String query) throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.execute(query);
        stmt.close();

    }

    public Long updateQueryWithGeneratedKey(String query) throws SQLException {

        Statement stmt = connection.createStatement();
        int result = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        //System.out.println("result=" + result);

        ResultSet rs = stmt.getGeneratedKeys();
        Long key = null;

        while (rs.next()) {
            key = rs.getLong(1);
        }

        rs.close();
        stmt.close();

        return key;
    }

    public Long updateQueryWithGeneratedKey(String query, Map<Integer, Object> args, String keyName) throws SQLException {

        PreparedStatement stmt = null;
        if (updateWithKeysPrepareds.containsKey(query)) {
            stmt = updateWithKeysPrepareds.get(query);
        } else {
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            updateWithKeysPrepareds.put(query, stmt);
        }

        stmt.clearParameters();

        //PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }

        //System.out.println("updateQueryWithKey: stmt=" + stmt.toString());

        int result = stmt.executeUpdate();
        //System.out.println("result=" + result);

        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        Long key = rs.getLong(keyName);

        rs.close();
        //stmt.close();

        return key;
    }

    public void updateQuery(String query, Map<Integer, Object> args) throws SQLException {


        PreparedStatement stmt = null;
        if (updatePrepareds.containsKey(query)) {
            stmt = updatePrepareds.get(query);
        } else {
            stmt = connection.prepareStatement(query);
            updatePrepareds.put(query, stmt);
        }

        stmt.clearParameters();

       // PreparedStatement stmt = connection.prepareStatement(query);
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }
        stmt.executeQuery();
        //stmt.close();

    }

    public void close() {
        try {
            for (Map.Entry<String, PreparedStatement> entry : execPrepareds.entrySet()) {
                entry.getValue().close();
            }
            for (Map.Entry<String, PreparedStatement> entry : updatePrepareds.entrySet()) {
                entry.getValue().close();
            }
            for (Map.Entry<String, PreparedStatement> entry : updateWithKeysPrepareds.entrySet()) {
                entry.getValue().close();
            }
        } catch (SQLException sqlExc) {
            System.err.println("QUERY EXECUTOR::CLOSE : failed to close some of prepared statements");
        }
    }

}

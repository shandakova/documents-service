package com.shandakova.documents.dao;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.entities.DocumentType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DocumentTypeDAO {
    private final ConnectionPool connectionPool;

    public DocumentTypeDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<DocumentType> getAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        String SELECT_ALL_TYPES = "SELECT * FROM types";
        statement.execute(SELECT_ALL_TYPES);
        ResultSet res = statement.getResultSet();
        connectionPool.returnConnection(connection);
        return parseResultListTypes(res);
    }

    private List<DocumentType> parseResultListTypes(ResultSet set) throws SQLException {
        List<DocumentType> res = new ArrayList<>();
        while (set.next()) {
            DocumentType documentType = new DocumentType();
            documentType.setId(set.getInt("id"));
            documentType.setName(set.getString("name"));
            res.add(documentType);
        }
        return res;
    }
}

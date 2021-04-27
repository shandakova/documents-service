package com.shandakova.documents.dao.impl;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.interfaces.DocumentTypeDAO;
import com.shandakova.documents.entities.DocumentType;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DocumentTypeDAOImpl implements DocumentTypeDAO {
    private final ConnectionPool connectionPool;
    private final String SELECT_ALL_TYPES = "SELECT * FROM types";

    public DocumentTypeDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<DocumentType> getAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(SELECT_ALL_TYPES);
        ResultSet res = statement.getResultSet();
        connectionPool.returnConnection(connection);
        log.info("Get all types.");
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

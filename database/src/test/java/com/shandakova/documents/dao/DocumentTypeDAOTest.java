package com.shandakova.documents.dao;


import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.config.AppConfig;
import com.shandakova.documents.dao.impl.DocumentTypeDAOImpl;
import com.shandakova.documents.entities.DocumentType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class DocumentTypeDAOTest {
    @Autowired
    private DocumentTypeDAOImpl documentTypeDAO;
    @Autowired
    private ConnectionPool connectionPool;

    @After
    public void shutdown() throws SQLException {
        connectionPool.clear();
    }

    @Test
    public void getAll() throws SQLException {
        List<DocumentType> types = documentTypeDAO.getAll();

        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM TYPES");
        ResultSet set = statement.getResultSet();
        int expectedSize = 0;
        while (set.next()) {
            DocumentType documentType = new DocumentType();
            documentType.setId(set.getInt("id"));
            documentType.setName(set.getString("name"));
            expectedSize++;
            assertEquals(documentType.getName(), findTypeNameByIdInList(documentType.getId(), types));
        }
        connectionPool.returnConnection(connection);
        assertEquals(expectedSize, types.size());
    }

    private String findTypeNameByIdInList(int id, List<DocumentType> types) {
        return types.stream().filter(documentType -> documentType.getId().equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("There are no type with id:" + id)).getName();
    }

}
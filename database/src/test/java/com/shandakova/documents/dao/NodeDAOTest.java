package com.shandakova.documents.dao;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.entities.Directory;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.Node;
import com.shandakova.documents.entities.enums.Importance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeDAOTest {
    NodeDAO nodeDAO;
    DocumentsDAO documentsDAO;
    DirectoriesDAO directoriesDAO;
    DocumentTypeDAO documentTypeDAO;

    @Before
    public void init() throws SQLException, IOException {
        ConnectionPool connectionPool = ConnectionPool.getInstanceByProperties("database.properties");
        documentsDAO = new DocumentsDAO(connectionPool);
        directoriesDAO = new DirectoriesDAO(connectionPool);
        documentTypeDAO = new DocumentTypeDAO(connectionPool);
        nodeDAO = new NodeDAO(connectionPool);
    }

    @After
    public void shutdown() throws SQLException {
        directoriesDAO.deleteAll();
        documentsDAO.deleteAll();
    }

    @Test
    public void testGetNodesByParentId() throws SQLException {
        int num = 5;
        for (int i = 0; i < num; i++) {
            Document document = new Document();
            fillDocument(document);
            documentsDAO.createNewDocument(document);
            Directory directory = new Directory();
            fillDirectory(directory);
            directoriesDAO.create(directory);
        }
        List<Node> nodesList = nodeDAO.getNodesByParentId(null, true);
        assertEquals(num * 2, nodesList.size());
        for (int i = 0; i < num * 2 - 1; i++) {
            LocalDateTime curr = nodesList.get(i).getCreationDateTime();
            LocalDateTime next = nodesList.get(i + 1).getCreationDateTime();
            assertTrue(curr.isAfter(next));
        }
    }

    private void fillDirectory(Directory directory) {
        directory.setName("test-directory" + LocalDateTime.now());
        directory.setParentId(null);
        directory.setAvailable(true);
    }

    private void fillDocument(Document document) throws SQLException {
        document.setName("test-document" + LocalDateTime.now());
        document.setParentId(null);
        document.setImportance(Importance.LOW);
        document.setDescription("This is test document!");
        int type = documentTypeDAO.getAll().get(0).getId();
        document.setTypeId(type);
    }

}
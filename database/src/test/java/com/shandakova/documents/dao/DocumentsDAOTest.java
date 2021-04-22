package com.shandakova.documents.dao;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.enums.Importance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentsDAOTest {
    private DocumentsDAO documentsDAO;
    private DocumentTypeDAO documentTypeDAO;

    @Before
    public void init() throws SQLException, IOException {
        ConnectionPool connectionPool = ConnectionPool.getInstanceByProperties("database.properties");
        documentsDAO = new DocumentsDAO(connectionPool);
        documentTypeDAO = new DocumentTypeDAO(connectionPool);
        documentsDAO.deleteAll();
    }

    @After
    public void shutdown() throws SQLException {
        documentsDAO.deleteAll();
    }

    @Test
    public void createNewDocument() throws SQLException {
        Document document = createAndFillTestDocument();
        documentsDAO.createNewDocument(document);
        Document created = documentsDAO.findAll().get(0);
        assertEquals(document.getName(), created.getName());
        assertEquals(document.getDescription(), created.getDescription());
        assertEquals(document.getImportance(), created.getImportance());
        assertEquals(0, created.getVersionNumber());
        assertNull(created.getPreviousVersionId());
        assertNotNull(created.getCreationDateTime());
    }

    @Test
    public void createNewDocumentVersion() throws SQLException {
        Document document = createAndFillTestDocument();
        documentsDAO.createNewDocument(document);
        Document create = documentsDAO.findAll().get(0);
        Document newVersion = documentsDAO.findAll().get(0);
        newVersion.setImportance(Importance.HIGH);
        newVersion.setDescription("This is new version of test documents!");
        documentsDAO.createNewVersionByDocument(create, newVersion);
        List<Document> documents = documentsDAO.findAll();
        assertEquals(2, documents.size());
        Document newVersionDB, oldVersionDB;
        if (documents.get(0).getCreationDateTime().isAfter(documents.get(1).getCreationDateTime())) {
            newVersionDB = documents.get(0);
            oldVersionDB = documents.get(1);
        } else {
            newVersionDB = documents.get(1);
            oldVersionDB = documents.get(0);
        }
        assertNotNull(newVersionDB.getPreviousVersionId());
        assertNull(oldVersionDB.getPreviousVersionId());
        assertEquals((int) newVersionDB.getPreviousVersionId(), oldVersionDB.getId());
        assertEquals(newVersionDB.getName(), oldVersionDB.getName());
        assertEquals(1, newVersionDB.getVersionNumber());
        assertEquals(newVersion.getImportance(), newVersionDB.getImportance());
        assertEquals(newVersion.getDescription(), newVersionDB.getDescription());
    }

    private Document createAndFillTestDocument() throws SQLException {
        Document document = new Document();
        document.setName("test-document" + LocalDateTime.now());
        document.setParentId(null);
        document.setAvailable(true);
        document.setImportance(Importance.LOW);
        document.setDescription("This is test document!");
        int type = documentTypeDAO.getAll().get(0).getId();
        document.setTypeId(type);
        return document;
    }

    @Test
    public void findAllDocumentsByParentId() throws SQLException {
        int dirNumber = 10;
        for (int i = 0; i < dirNumber; i++) {
            documentsDAO.createNewDocument(createAndFillTestDocument());
        }
        List<Document> documents = documentsDAO.findAllDocumentsByParentId(null, true);
        assertEquals(dirNumber, documents.size());
        for (int i = 0; i < dirNumber - 1; i++) {
            LocalDateTime curr = documents.get(i).getCreationDateTime();
            LocalDateTime next = documents.get(i + 1).getCreationDateTime();
            assertTrue(curr.isAfter(next));
        }
    }
}
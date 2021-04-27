package com.shandakova.documents.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.impl.DocumentTypeDAOImpl;
import com.shandakova.documents.dao.impl.DocumentsDAOImpl;
import com.shandakova.documents.dao.interfaces.DocumentTypeDAO;
import com.shandakova.documents.dao.interfaces.DocumentsDAO;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.entities.Document;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentServletTest {
    private static DocumentsDAO documentsDAO;
    private static DocumentTypeDAO documentTypeDAO;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static Server server;

    @BeforeClass
    public static void init() throws Exception {
        server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);
        WebAppContext root = new WebAppContext("src/main/webapp", "/");
        server.setHandlers(new Handler[]{root});
        server.start();
        documentsDAO = new DocumentsDAOImpl(ConnectionPool.getInstanceByProperties("database.properties"));
        documentTypeDAO = new DocumentTypeDAOImpl(ConnectionPool.getInstanceByProperties("database.properties"));
    }

    @Before
    public void clear() throws Exception {
        documentsDAO.deleteAll();
    }

    @Test
    public void doPostNewDocument() throws Exception {
        URI serverUri = new URI("http://localhost:8080/");
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/document").toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        OutputStream outputStream = http.getOutputStream();
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDescription("Description");
        documentDTO.setName("doc");
        documentDTO.setTypeId(documentTypeDAO.getAll().get(0).getId());
        objectMapper.writeValue(outputStream, documentDTO);
        http.connect();
        assertEquals(HttpStatus.ORDINAL_201_Created, http.getResponseCode());
        Document document = documentsDAO.findAll().get(0);
        assertEquals("Description", document.getDescription());
        assertEquals("doc", document.getName());
        assertEquals(documentDTO.getTypeId(), document.getTypeId());
        assertNotNull(document.getId());
        assertNotNull(document.getImportance());
        assertNotNull(document.getCreationDateTime());
    }

    @Test
    public void doPostNewVersion() throws Exception {
        Document doc = createDocument();
        DocumentDTO documentDTO = new DocumentDTO();
        fillDTObyDocument(doc, documentDTO);
        documentDTO.setName("new-name");
        URI serverUri = new URI("http://localhost:8080/");
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/document/" + doc.getId() + "/version").toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        OutputStream outputStream = http.getOutputStream();
        objectMapper.writeValue(outputStream, documentDTO);
        http.connect();
        assertEquals(HttpStatus.ORDINAL_201_Created, http.getResponseCode());
        List<Document> docs = documentsDAO.findAll();
        assertEquals(2, docs.size());
        assertTrue(docs.stream().anyMatch(document -> document.getId().equals(doc.getId()) &&
                document.getName().equals(doc.getName()) &&
                document.getDescription().equals(doc.getDescription())));
        assertTrue(docs.stream().anyMatch(document -> doc.getId().equals(document.getPreviousVersionId()) &&
                document.getName().equals("new-name") &&
                document.getDescription().equals(doc.getDescription())));
    }

    private void fillDTObyDocument(Document document, DocumentDTO documentDTO) {
        documentDTO.setName(document.getName());
        documentDTO.setId(document.getId());
        documentDTO.setDescription(document.getDescription());
        documentDTO.setImportance(document.getImportance());
        documentDTO.setAvailable(document.isAvailable());
        documentDTO.setTypeId(document.getTypeId());
    }

    private Document createDocument() throws SQLException {
        Document document = new Document();
        document.setName("name");
        document.setDescription("Description");
        document.setTypeId(documentTypeDAO.getAll().get(0).getId());
        documentsDAO.createNewDocument(document);
        return documentsDAO.findAll().get(0);
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stop();
    }
}
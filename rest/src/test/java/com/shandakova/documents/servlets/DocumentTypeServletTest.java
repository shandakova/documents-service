package com.shandakova.documents.servlets;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.impl.DocumentTypeDAOImpl;
import com.shandakova.documents.dao.interfaces.DocumentTypeDAO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentTypeServletTest {
    private static DocumentTypeDAO documentTypeDAO;
    private static Server server;
    private static final int PORT = 8080;
    private final String URI = "http://localhost:" + PORT + "/";
    private static final String PROPERTIES = "database.properties";

    @BeforeClass
    public static void init() throws Exception {
        server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.addConnector(connector);
        WebAppContext root = new WebAppContext("src/main/webapp", "/");
        server.setHandlers(new Handler[]{root});
        server.start();
        documentTypeDAO = new DocumentTypeDAOImpl(ConnectionPool.getInstanceByProperties(PROPERTIES));
    }

    @Test
    public void testGetAll() throws Exception {
        URI serverUri = new URI(URI);
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/type/get/all").toURL().openConnection();
        http.connect();
        assertEquals(HttpStatus.ORDINAL_200_OK, http.getResponseCode());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
            String response = br.lines().collect(Collectors.joining());
            assertTrue(response.contains("\"name\":\"Письмо\""));
            assertTrue(response.contains("\"name\":\"Факс\""));
            assertTrue(response.contains("\"name\":\"Приказ\""));
        }
        http.disconnect();
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stop();
    }

}
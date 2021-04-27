package com.shandakova.documents.servlets;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.impl.DirectoriesDAOImpl;
import com.shandakova.documents.dao.interfaces.DirectoriesDAO;
import com.shandakova.documents.entities.Directory;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeServletTest {
    private static DirectoriesDAO directoriesDAO;
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
        directoriesDAO = new DirectoriesDAOImpl(ConnectionPool.getInstanceByProperties("database.properties"));
    }

    @Before
    public void setUp() throws Exception {
        directoriesDAO.deleteAll();
    }

    @Test
    public void doGet() throws Exception {
        createTestDirectory();
        URI serverUri = new URI("http://localhost:8080/");
        HttpURLConnection http = (HttpURLConnection) serverUri.
                resolve("/node/get/all?parent_id=null&order=desc").toURL().openConnection();
        http.setRequestMethod("GET");
        http.connect();
        assertEquals(HttpStatus.ORDINAL_200_OK, http.getResponseCode());
        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String response = br.lines().collect(Collectors.joining());
        System.out.println(response);
        assertTrue(response.contains("\"name\":\"dir\""));
        assertTrue(response.contains("\"parentId\":null"));

    }

    private void createTestDirectory() throws SQLException {
        Directory directory = new Directory();
        directory.setName("dir");
        directory.setParentId(null);
        directoriesDAO.create(directory);
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stop();
    }
}
package com.shandakova.documents.servlets;

import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dao.config.AppConfig;
import com.shandakova.documents.entities.Directory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class NodeServletTest {
    @Qualifier("directoriesDaoJpaImpl")
    @Autowired
    private DirectoriesDAO directoriesDAO;
    private static Server server;
    private static final int PORT = 8080;
    private final String URI = "http://localhost:" + PORT + "/";

    @BeforeClass
    public static void init() throws Exception {
        server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.addConnector(connector);
        WebAppContext root = new WebAppContext("src/main/webapp", "/");
        server.setHandlers(new Handler[]{root});
        server.start();
    }

    @Before
    public void setUp() throws Exception {
        directoriesDAO.deleteAll();
    }

    @Test
    public void doGet() throws Exception {
        createTestDirectory();
        URI serverUri = new URI(URI);
        HttpURLConnection http = (HttpURLConnection) serverUri.
                resolve("/node/get/all?parent_id=null&order=desc").toURL().openConnection();
        http.setRequestMethod("GET");
        http.connect();
        assertEquals(HttpStatus.ORDINAL_200_OK, http.getResponseCode());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
            String response = br.lines().collect(Collectors.joining());
            assertTrue(response.contains("\"name\":\"dir\""));
            assertTrue(response.contains("\"parentId\":null"));
        }
        http.disconnect();
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
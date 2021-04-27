package com.shandakova.documents.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.impl.DirectoriesDAOImpl;
import com.shandakova.documents.dao.interfaces.DirectoriesDAO;
import com.shandakova.documents.dto.DirectoryDTO;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoryServletTest {
    private static DirectoriesDAO directoriesDAO;
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
        directoriesDAO = new DirectoriesDAOImpl(ConnectionPool.getInstanceByProperties("database.properties"));
    }

    @Before
    public void clear() throws SQLException {
        directoriesDAO.deleteAll();
    }

    @Test
    public void doPostOneDirectory() throws Exception {
        URI serverUri = new URI("http://localhost:8080/");
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/directory").toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        OutputStream outputStream = http.getOutputStream();
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setName("dir");
        objectMapper.writeValue(outputStream, directoryDTO);
        http.connect();
        assertEquals(HttpStatus.ORDINAL_201_Created, http.getResponseCode());
        List<Directory> allDirectories = directoriesDAO.findAllDirectories();
        assertEquals(1, allDirectories.size());
        assertEquals("dir", allDirectories.get(0).getName());
    }

    @Test
    public void doPostListDirectories() throws Exception {
        URI serverUri = new URI("http://localhost:8080/");
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/directory/all").toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        OutputStream outputStream = http.getOutputStream();
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setName("dir1");
        DirectoryDTO directoryDTO2 = new DirectoryDTO();
        directoryDTO2.setName("dir2");
        List<DirectoryDTO> directoryDTOList = Arrays.asList(directoryDTO, directoryDTO2);
        objectMapper.writeValue(outputStream, directoryDTOList);
        http.connect();
        assertEquals(HttpStatus.ORDINAL_201_Created, http.getResponseCode());
        List<Directory> allDirectories = directoriesDAO.findAllDirectories();
        assertEquals(2, allDirectories.size());
        assertTrue(allDirectories.stream().anyMatch(directory -> directory.getName().equals("dir1")));
        assertTrue(allDirectories.stream().anyMatch(directory -> directory.getName().equals("dir2")));
    }


    @Test
    public void doPut() throws Exception {
        Directory directory = new Directory();
        directory.setName("dir");
        directoriesDAO.create(directory);
        Integer id = directoriesDAO.findAllDirectories().get(0).getId();
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setId(id);
        directoryDTO.setName("new-dir-name");
        URI serverUri = new URI("http://localhost:8080/");
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/directory/" + id).toURL().openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        OutputStream outputStream = http.getOutputStream();
        objectMapper.writeValue(outputStream, directoryDTO);
        http.connect();
        assertEquals(HttpStatus.ORDINAL_200_OK, http.getResponseCode());
        assertEquals("new-dir-name", directoriesDAO.findAllDirectories().get(0).getName());
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stop();
    }
}
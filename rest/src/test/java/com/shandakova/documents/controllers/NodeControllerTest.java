package com.shandakova.documents.controllers;

import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dao.DocumentTypeDAO;
import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dao.config.AppConfig;
import com.shandakova.documents.dao.impl.jpa.repository.NodeRepository;
import com.shandakova.documents.entities.Directory;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.DocumentType;
import com.shandakova.documents.entities.enums.Importance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebMvcTest(NodeController.class)
public class NodeControllerTest {
    @Autowired
    private DirectoriesDAO dirDAO;
    @Autowired
    private DocumentsDAO docDAO;
    @Autowired
    private DocumentTypeDAO typeDAO;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private NodeRepository nodeRepository;

    @Before
    public void setup() {
        nodeRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void getAllNodesFindByTypeAndName() throws Exception {
        String name = "very unique name";
        createDirectoryAndDocumentWithSameName(name);
        MvcResult mvcResult = mvc.perform(get("/node/all/find")
                .param("type", "document")
                .param("name", name))
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        assertTrue(json.contains("\"name\":\"very unique name\""));
        assertTrue(json.contains("\"nodeType\":\"DOCUMENT\""));
        assertFalse(json.contains("\"nodeType\":\"DIRECTORY\""));
    }

    private void createDirectoryAndDocumentWithSameName(String name) throws SQLException {
        Document document = createAndFillTestDocument(name);
        docDAO.createNewDocument(document);
        Directory directory = createAndFillTestDirectory(name);
        dirDAO.create(directory);
    }

    private Document createAndFillTestDocument(String name) throws SQLException {
        Document document = new Document();
        document.setName(name);
        document.setParent(null);
        document.setAvailable(true);
        document.setImportance(Importance.LOW);
        document.setDescription("This is test document!");
        DocumentType type = typeDAO.getAll().get(0);
        document.setType(type);
        return document;
    }

    private Directory createAndFillTestDirectory(String name) throws SQLException {
        Directory directory = new Directory();
        directory.setName(name);
        return directory;
    }

    @After
    public void shutdown() {
        nodeRepository.deleteAll();
    }
}
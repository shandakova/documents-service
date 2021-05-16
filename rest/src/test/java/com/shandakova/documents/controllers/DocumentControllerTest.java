package com.shandakova.documents.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dao.config.AppConfig;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentTypeRepository;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentsRepository;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.entities.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {
    @Autowired
    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DocumentsRepository documentsRepository;
    @Autowired
    private DocumentsDAO documentsDAO;

    @Before
    public void setup() {
        documentsRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void createDocument() throws Exception {
        DocumentDTO documentDTO = new DocumentDTO();
        String documentName = "document";
        String description = "new cool document";
        documentDTO.setName(documentName);
        documentDTO.setDescription(description);
        documentDTO.setType(documentTypeRepository.findAll().get(0));
        MvcResult mvcResult = mvc.perform(post("/document/")
                .content(objectMapper.writeValueAsString(documentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Created document"));
        List<Document> documentList = documentsRepository.findAll();
        assertEquals(1, documentList.size());
        Document doc = documentsRepository.findAll().get(0);
        assertEquals(documentName, doc.getName());
        assertEquals(description, doc.getDescription());
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void createNewVersion() throws Exception {
        Document document = new Document();
        document.setName("old version");
        documentsDAO.createNewDocument(document);
        List<Document> documentList = documentsRepository.findAll();
        assertEquals(1, documentList.size());
        Document created = documentsRepository.findAll().get(0);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setName("new version");
        documentDTO.setParentDocument(created.getId());
        MvcResult mvcResult = mvc.perform(post("/document/" + created.getId() + "/new-version")
                .content(objectMapper.writeValueAsString(documentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        documentList = documentsRepository.findAll();
        assertEquals(2, documentList.size());
        assertNotNull(documentList.stream().filter(d -> d.getName().equals("new version") &&
                d.getPreviousVersion().getId().equals(created.getId())).findAny());
        assertNotNull(documentList.stream().filter(d -> d.getName().equals("old version")));
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void findVersions() throws Exception {
        Document document = new Document();
        document.setName("old version");
        documentsDAO.createNewDocument(document);
        Document created = documentsRepository.findAll().get(0);
        Document newVer1 = new Document();
        newVer1.setName("new ver1");
        documentsDAO.createNewVersionByDocument(created.getId(), newVer1);
        Document newVer2 = new Document();
        newVer2.setName("new ver2");
        documentsDAO.createNewVersionByDocument(created.getId(), newVer2);
        MvcResult mvcResult = mvc.perform(get("/document/" + created.getId() + "/versions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("new ver1"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("new ver2"));
    }


    @After
    public void shutdown() {
        documentsRepository.deleteAll();
    }

}
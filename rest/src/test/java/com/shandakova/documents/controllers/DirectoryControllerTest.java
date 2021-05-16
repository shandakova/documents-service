package com.shandakova.documents.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dao.config.AppConfig;
import com.shandakova.documents.dao.impl.jpa.repository.DirectoriesRepository;
import com.shandakova.documents.dto.DirectoryDTO;
import com.shandakova.documents.entities.Directory;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebMvcTest(DirectoryController.class)
public class DirectoryControllerTest {
    @Autowired
    private DirectoriesDAO dirDAO;
    @Autowired
    private DirectoriesRepository rep;
    @Autowired
    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        rep.deleteAll();
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void createDirectory() throws Exception {
        DirectoryDTO directoryDTO = new DirectoryDTO();
        String dirName = "test-directory";
        directoryDTO.setName(dirName);
        MvcResult mvcResult = mvc.perform(post("/directory/")
                .content(objectMapper.writeValueAsString(directoryDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Created directory"));
        List<Directory> directoryList = rep.findAll();
        assertEquals(1, directoryList.size());
        Directory directory = directoryList.stream().findFirst().get();
        assertEquals(dirName, directory.getName());
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void creatManyDirectory() throws Exception {
        String dirName = "test-directory";
        int numberDir = 5;
        List<DirectoryDTO> directories = createTestDirectories(dirName, numberDir);
        MvcResult mvcResult = mvc.perform(post("/directory/all")
                .content(objectMapper.writeValueAsString(directories))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Created directories"));
        List<Directory> directoryList = rep.findAll();
        assertEquals(numberDir, directoryList.size());
        for (int i = 0; i < numberDir; i++) {
            int finalI = i;
            assertEquals(1, directoryList.stream()
                    .filter(d -> d.getName().equals(dirName + "-" + finalI)).count());
        }
    }

    private List<DirectoryDTO> createTestDirectories(String dirName, int num) {
        List<DirectoryDTO> directories = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DirectoryDTO directoryDTO = new DirectoryDTO();
            directoryDTO.setName(dirName + "-" + i);
            directories.add(directoryDTO);
        }
        return directories;
    }

    @Test
    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    public void updateById() throws Exception {
        Directory d = new Directory();
        d.setName("some-name");
        dirDAO.create(d);
        d = rep.findAll().stream().findFirst().get();
        DirectoryDTO directoryDTO = new DirectoryDTO();
        directoryDTO.setId(d.getId());
        String newName = "new-name";
        directoryDTO.setName(newName);
        MvcResult mvcResult = mvc.perform(put("/directory/" + d.getId())
                .content(objectMapper.writeValueAsString(directoryDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Updated directory"));
        List<Directory> directoryList = rep.findAll();
        assertEquals(1, directoryList.size());
        Directory directory = directoryList.stream().findFirst().get();
        assertEquals(newName, directory.getName());
    }

    @After
    public void shutdown() {
        rep.deleteAll();
    }
}
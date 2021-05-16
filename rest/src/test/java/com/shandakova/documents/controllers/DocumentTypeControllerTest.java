package com.shandakova.documents.controllers;

import com.shandakova.documents.dao.config.AppConfig;
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

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebMvcTest(DocumentTypeController.class)
public class DocumentTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(username = "sly_zucchini", password = "qwerty")
    @Test
    public void getAllTypes() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/type/all")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("Письмо"));
        assertTrue(response.contains("Факс"));
        assertTrue(response.contains("Приказ"));
    }
}
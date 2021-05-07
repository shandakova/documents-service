package com.shandakova.documents.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dto.DocumentTypeDTO;
import com.shandakova.documents.services.DocumentTypeService;
import com.shandakova.documents.services.impl.DocumentTypeServiceImpl;
import com.shandakova.documents.servlets.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class DocumentTypeServlet extends HttpServlet {
    private DocumentTypeService documentTypeService;

    public DocumentTypeServlet() {
        super();
        documentTypeService = ApplicationContextHolder.getApplicationContext().getBean(DocumentTypeServiceImpl.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<DocumentTypeDTO> types = documentTypeService.getAll();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(types);
            resp.getOutputStream().write(jsonString.getBytes(StandardCharsets.UTF_8));
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            log.info("GET REQUEST: all types.");
        } catch (Exception e) {
            log.error("GET REQUEST: can't execute request. ", e);
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        }
    }
}

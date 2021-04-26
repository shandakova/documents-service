package com.shandakova.documents.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dto.DocumentTypeDTO;
import com.shandakova.documents.services.DocumentTypeService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class DocumentTypeServlet extends HttpServlet {
    private DocumentTypeService documentTypeService;

    public DocumentTypeServlet() throws SQLException, IOException {
        super();
        documentTypeService = DocumentTypeService.getInstance("database.properties");
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

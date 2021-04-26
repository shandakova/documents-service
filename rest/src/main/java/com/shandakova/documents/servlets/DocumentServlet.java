package com.shandakova.documents.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.services.DocumentService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class DocumentServlet extends HttpServlet {
    private DocumentService documentService;

    public DocumentServlet() throws SQLException, IOException {
        super();
        documentService = DocumentService.getInstance("database.properties");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getRequestURI();
        try {
            String jsonPost = getStringFromRequest(req);
            ObjectMapper mapper = new ObjectMapper();
            if (path.equals("/document")) {
                DocumentDTO documentDTO = mapper.readValue(jsonPost, DocumentDTO.class);
                documentService.create(documentDTO);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                log.info("POST REQUEST: create document ");
            } else if (containId(path)) {
                DocumentDTO documentDTO = mapper.readValue(jsonPost, DocumentDTO.class);
                String idStr = path.replace("/document/", "").replace("/version", "");
                Integer id = Integer.parseInt(idStr);
                if (id.equals(documentDTO.getId())) {
                    documentService.createNewVersion(documentDTO);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    log.info("POST REQUEST: create new version document with id" + documentDTO.getId());
                } else {
                    log.error("POST REQUEST: error while compare id.");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                log.error("POST REQUEST: error while read request. ");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (IOException e) {
            log.error("POST REQUEST: error while read request. ", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            log.error("POST REQUEST: error while save directory. ", e);
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        }
    }

    private boolean containId(String s) {
        return s.replace("/document/", "")
                .replace("/version", "").matches("[0-9]+");
    }

    private String getStringFromRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        return reader.lines().collect(Collectors.joining());
    }

}

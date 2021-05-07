package com.shandakova.documents.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dto.DirectoryDTO;
import com.shandakova.documents.services.DirectoriesService;
import com.shandakova.documents.services.impl.DirectoriesServiceImpl;
import com.shandakova.documents.servlets.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DirectoryServlet extends HttpServlet {
    private DirectoriesService directoriesService;

    public DirectoryServlet() {
        super();
        directoriesService = ApplicationContextHolder.getApplicationContext().getBean(DirectoriesServiceImpl.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getRequestURI();
        try {
            String jsonPost = getStringFromRequest(req);
            ObjectMapper mapper = new ObjectMapper();
            if (path.equals("/directory")) {
                DirectoryDTO directoryDTO = mapper.readValue(jsonPost, DirectoryDTO.class);
                directoriesService.create(directoryDTO);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else if (path.equals("/directory/all")) {
                List<DirectoryDTO> directoriesDTO =
                        mapper.readValue(jsonPost, new TypeReference<List<DirectoryDTO>>() {
                        });
                directoriesService.createMany(directoriesDTO);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
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

    private String getStringFromRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        return reader.lines().collect(Collectors.joining());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer id = Integer.parseInt(req.getRequestURI().replace("/directory/", ""));
            String jsonPut = getStringFromRequest(req);
            ObjectMapper mapper = new ObjectMapper();
            DirectoryDTO directoryDTO = mapper.readValue(jsonPut, DirectoryDTO.class);
            directoryDTO.setId(id);
            directoriesService.update(directoryDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            log.error("PUT REQUEST: error while read id from request.", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            log.error("PUT REQUEST: error while read request. ", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            log.error("PUT REQUEST: error while update directory. ", e);
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        }
    }
}

package com.shandakova.documents.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shandakova.documents.dto.NodeDTO;
import com.shandakova.documents.services.NodeService;
import com.shandakova.documents.servlets.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class NodeServlet extends HttpServlet {
    private NodeService nodeService;

    public NodeServlet() {
        nodeService = ApplicationContextHolder.getApplicationContext().getBean(NodeService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String[] queryString = req.getQueryString().split("&");
            Integer parentId = null;
            boolean order = false;
            int count = 0;
            for (String s : queryString) {
                if (s.contains("order=")) {
                    order = isOrder(resp, order, s);
                    count++;
                } else if (s.contains("parent_id=")) {
                    parentId = getInteger(s);
                    count++;
                }
            }
            if (count != 2) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                log.error("GET REQUEST: error number of parameters");
            } else {
                List<NodeDTO> nodes = nodeService.getAllByParentId(parentId, order);
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(nodes);
                resp.getOutputStream().write(jsonString.getBytes(StandardCharsets.UTF_8));
                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                log.info("GET REQUEST: all nodes by parent id " + parentId);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            log.error("GET REQUEST: error while get all nodes", e);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            log.error("GET REQUEST: error while write json", e);
        }
    }

    private Integer getInteger(String s) {
        Integer parentId;
        String idStr = s.replace("parent_id=", "").toLowerCase();
        if (idStr.equals("null")) {
            parentId = null;
        } else {
            parentId = Integer.parseInt(idStr);
        }
        return parentId;
    }

    private boolean isOrder(HttpServletResponse resp, boolean order, String s) {
        String o = s.replace("order=", "").toLowerCase();
        if (o.equals("desc")) {
            order = true;
        } else if (o.equals("asc")) {
            order = false;
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return order;
    }
}

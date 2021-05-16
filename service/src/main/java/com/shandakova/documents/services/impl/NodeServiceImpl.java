package com.shandakova.documents.services.impl;

import com.shandakova.documents.dao.NodeDAO;
import com.shandakova.documents.dto.NodeDTO;
import com.shandakova.documents.entities.Node;
import com.shandakova.documents.entities.enums.NodeType;
import com.shandakova.documents.services.NodeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NodeServiceImpl implements NodeService {
    private NodeDAO nodeDAO;

    public NodeServiceImpl(@Qualifier("nodeDaoJpaImpl") NodeDAO nodeDAO) {
        this.nodeDAO = nodeDAO;
    }

    public List<NodeDTO> getAllByParentId(Integer parentId, boolean order) throws SQLException {
        List<Node> nodesByParentId = nodeDAO.getNodesByParentId(parentId, order);
        return getListDTObyNodes(nodesByParentId);
    }

    private List<NodeDTO> getListDTObyNodes(List<Node> nodesByParentId) {
        List<NodeDTO> nodes = new ArrayList<>();
        nodesByParentId.forEach(n -> {
                    NodeDTO node = new NodeDTO();
                    fillDTObyNode(n, node);
                    nodes.add(node);
                }
        );
        return nodes;
    }

    public List<NodeDTO> getListByQuery(Map<String, String> query) throws SQLException {
        List<Node> nodes = new ArrayList<>();
        if (query.containsKey("parent-id")) {
            if (!query.get("parent-id").equals("null")) {
                Integer id = Integer.valueOf(query.get("parent-id"));
                nodes.addAll(nodeDAO.getNodesByParentId(id, false));
            } else {
                nodes.addAll(nodeDAO.getNodesByParentId(null, false));
            }
        }
        if (query.containsKey("name")) {
            if (nodes.size() == 0) {
                nodes.addAll(nodeDAO.getNodesByName(query.get("name")));
            } else {
                nodes.removeIf(n -> !n.getName().contains(query.get("name")));
            }
        }
        if (query.containsKey("type")) {
            String type = query.get("type").toUpperCase();
            if (!type.equals(NodeType.Values.DIRECTORY) && !type.equals(NodeType.Values.DOCUMENT)) {
                throw new IllegalArgumentException("Invalid type ");
            }
            if (nodes.size() == 0) {
                nodes.addAll(nodeDAO.getNodesByType(NodeType.valueOf(type)));
            } else {
                nodes.removeIf(n -> !n.getNodeType().equals(type));
            }
        }
        return getListDTObyNodes(nodes);
    }

    private void fillDTObyNode(Node n, NodeDTO node) {
        node.setName(n.getName());
        node.setId(n.getId());
        if (n.getParent() != null) {
            node.setParentId(n.getParent().getId());
        }
        node.setCreationDateTime(n.getCreationDateTime());
        node.setNodeType(n.getNodeType());
    }

}

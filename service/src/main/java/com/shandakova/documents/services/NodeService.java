package com.shandakova.documents.services;

import com.shandakova.documents.dao.NodeDAO;
import com.shandakova.documents.dto.NodeDTO;
import com.shandakova.documents.entities.Node;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NodeService {
    private NodeDAO nodeDAO;

    public NodeService(NodeDAO nodeDAO) {
        this.nodeDAO = nodeDAO;
    }

    public List<NodeDTO> getAllByParentId(Integer parentId, boolean order) throws SQLException {
        List<Node> nodesByParentId = nodeDAO.getNodesByParentId(parentId, order);
        List<NodeDTO> res = getListDTObyNodes(nodesByParentId);
        return res;
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

    private void fillDTObyNode(Node n, NodeDTO node) {
        node.setName(n.getName());
        node.setId(n.getId());
        node.setParentId(n.getParentId());
        node.setCreationDateTime(n.getCreationDateTime());
    }

}

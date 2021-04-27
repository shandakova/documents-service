package services;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.impl.NodeDAOImpl;
import com.shandakova.documents.dao.interfaces.NodeDAO;
import dto.NodeDTO;
import com.shandakova.documents.entities.Node;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeService {
    private static NodeDAO nodeDAO;

    private NodeService() {

    }

    public static NodeService getInstance(String properties) throws SQLException, IOException {
        nodeDAO = new NodeDAOImpl(ConnectionPool.getInstanceByProperties(properties));
        return new NodeService();
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

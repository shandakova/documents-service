package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dao.NodeDAO;
import com.shandakova.documents.dao.impl.jpa.repository.NodeRepository;
import com.shandakova.documents.entities.Directory;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.Node;
import com.shandakova.documents.entities.enums.NodeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("nodeDaoJpaImpl")
public class NodeDAOJpaImpl implements NodeDAO {
    private final DocumentsDAO documentsDAO;
    private final DirectoriesDAO directoriesDAO;
    private NodeRepository nodeRepository;

    public NodeDAOJpaImpl(@Qualifier("documentsDaoJpaImpl") DocumentsDAO documentsDAO,
                          @Qualifier("directoriesDaoJpaImpl") DirectoriesDAO directoriesDAO,
                          NodeRepository nodeRepository) {
        this.documentsDAO = documentsDAO;
        this.directoriesDAO = directoriesDAO;
        this.nodeRepository = nodeRepository;
    }

    public List<Node> getNodesByParentId(Integer parentId, boolean isDescOrder) throws SQLException {
        List<Directory> directories = directoriesDAO.findAllDirectoriesByParentId(parentId, isDescOrder);
        List<Document> documents = documentsDAO.findAllDocumentsByParentId(parentId, isDescOrder);
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(directories);
        nodes.addAll(documents);
        nodes.sort((o1, o2) -> {
            int res = 0;
            if (o1.getCreationDateTime().isAfter(o2.getCreationDateTime())) {
                res = 1;
            } else if (o1.getCreationDateTime().isEqual(o2.getCreationDateTime())) {
                res = 0;
            } else {
                res = -1;
            }
            if (isDescOrder) res *= -1;
            return res;
        });
        return nodes;
    }

    public List<Node> getNodesByName(String name) {
        return nodeRepository.findByNameContains(name);
    }

    public List<Node> getNodesByType(NodeType type) {
        List<Node> nodes;
        switch (type) {
            case DOCUMENT:
                nodes = nodeRepository.findByNodeType(NodeType.Values.DOCUMENT);
                break;
            case DIRECTORY:
                nodes = nodeRepository.findByNodeType(NodeType.Values.DIRECTORY);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return nodes;
    }

}

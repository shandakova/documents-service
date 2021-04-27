package com.shandakova.documents.dao.impl;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.interfaces.NodeDAO;
import com.shandakova.documents.entities.Directory;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.Node;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NodeDAOImpl implements NodeDAO {
    final DocumentsDAOImpl documentsDAOImpl;
    final DirectoriesDAOImpl directoriesDAO;

    public NodeDAOImpl(ConnectionPool connectionPool) {
        documentsDAOImpl = new DocumentsDAOImpl(connectionPool);
        directoriesDAO = new DirectoriesDAOImpl(connectionPool);
    }


    public List<Node> getNodesByParentId(Integer parentId, boolean isDescOrder) throws SQLException {
        List<Directory> directories = directoriesDAO.findAllDirectoriesByParentId(parentId, isDescOrder);
        List<Document> documents = documentsDAOImpl.findAllDocumentsByParentId(parentId, isDescOrder);
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
        log.info("Get nodes by parent Id.");
        return nodes;
    }


}

package com.shandakova.documents.dao;

import com.shandakova.documents.entities.Node;
import com.shandakova.documents.entities.enums.NodeType;

import java.sql.SQLException;
import java.util.List;

public interface NodeDAO {
    List<Node> getNodesByParentId(Integer parentId, boolean isDescOrder) throws SQLException;

    List<Node> getNodesByName(String name);

    List<Node> getNodesByType(NodeType type);
}

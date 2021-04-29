package com.shandakova.documents.dao;

import com.shandakova.documents.entities.Node;

import java.sql.SQLException;
import java.util.List;

public interface NodeDAO {
    List<Node> getNodesByParentId(Integer parentId, boolean isDescOrder) throws SQLException;
}

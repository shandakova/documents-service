package com.shandakova.documents.services;

import com.shandakova.documents.dto.NodeDTO;

import java.sql.SQLException;
import java.util.List;

public interface NodeService {
    List<NodeDTO> getAllByParentId(Integer parentId, boolean order) throws SQLException;
}

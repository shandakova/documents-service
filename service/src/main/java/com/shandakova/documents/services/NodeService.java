package com.shandakova.documents.services;

import com.shandakova.documents.dto.NodeDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface NodeService {
    List<NodeDTO> getAllByParentId(Integer parentId, boolean order) throws SQLException;

    List<NodeDTO> getListByQuery(Map<String, String> query) throws SQLException;
}

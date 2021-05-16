package com.shandakova.documents.services;

import com.shandakova.documents.dto.DocumentDTO;

import java.sql.SQLException;
import java.util.List;

public interface DocumentService {
    void create(DocumentDTO documentDTO) throws SQLException;

    void createNewVersion(DocumentDTO documentDTO) throws SQLException;

    List<DocumentDTO> getAllVersionOfDocumentWithId(Integer id) throws SQLException;
}

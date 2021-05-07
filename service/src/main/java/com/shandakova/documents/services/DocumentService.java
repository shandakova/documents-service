package com.shandakova.documents.services;

import com.shandakova.documents.dto.DocumentDTO;

import java.sql.SQLException;

public interface DocumentService {
    void create(DocumentDTO documentDTO) throws SQLException;

    void createNewVersion(DocumentDTO documentDTO) throws SQLException;
}

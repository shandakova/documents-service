package com.shandakova.documents.dao;

import com.shandakova.documents.entities.Document;

import java.sql.SQLException;
import java.util.List;

public interface DocumentsDAO {
    List<Document> findAllDocumentsByParentId(Integer id, boolean isDescOrder) throws SQLException;
    void createNewDocument(Document document) throws SQLException;

    void createNewVersionByDocument(Integer oldVersionId, Document newVersion) throws SQLException;

    List<Document> findAll() throws SQLException;
    void deleteAll() throws SQLException;
}

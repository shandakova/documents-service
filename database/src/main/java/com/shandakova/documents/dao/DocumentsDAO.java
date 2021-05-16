package com.shandakova.documents.dao;

import com.shandakova.documents.entities.Document;

import java.sql.SQLException;
import java.util.List;

public interface DocumentsDAO {
    List<Document> findAllDocumentsByParentId(Integer id, boolean isDescOrder) throws SQLException;

    Document createNewDocument(Document document) throws SQLException;

    Document createNewVersionByDocument(Integer oldVersionId, Document newVersion) throws SQLException;

    List<Document> findAll() throws SQLException;

    void deleteAll() throws SQLException;

    List<Document> getVersionByDocumentId(Integer id) throws SQLException;
}

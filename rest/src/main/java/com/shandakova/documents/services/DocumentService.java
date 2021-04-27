package com.shandakova.documents.services;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.impl.DocumentTypeDAOImpl;
import com.shandakova.documents.dao.impl.DocumentsDAOImpl;
import com.shandakova.documents.dao.interfaces.DocumentTypeDAO;
import com.shandakova.documents.dao.interfaces.DocumentsDAO;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.entities.Document;

import java.io.IOException;
import java.sql.SQLException;

public class DocumentService {
    private static DocumentsDAO documentsDAO;
    private static DocumentTypeDAO documentTypeDAO;

    private DocumentService() {
    }

    public static DocumentService getInstance(String properties) throws SQLException, IOException {
        ConnectionPool connectionPool = ConnectionPool.getInstanceByProperties(properties);
        documentsDAO = new DocumentsDAOImpl(connectionPool);
        documentTypeDAO = new DocumentTypeDAOImpl(connectionPool);
        return new DocumentService();
    }

    public void create(DocumentDTO documentDTO) throws SQLException {
        Document document = new Document();
        fillDocumentByDocumentDTO(documentDTO, document);
        documentsDAO.createNewDocument(document);
    }

    private void fillDocumentByDocumentDTO(DocumentDTO documentDTO, Document document) {
        document.setDescription(documentDTO.getDescription());
        document.setImportance(documentDTO.getImportance());
        document.setName(documentDTO.getName());
        document.setTypeId(documentDTO.getTypeId());
        document.setParentId(documentDTO.getParentId());
        document.setVersionNumber(documentDTO.getVersionNumber());
    }

    public void createNewVersion(DocumentDTO documentDTO) throws SQLException {
        Document document = new Document();
        fillDocumentByDocumentDTO(documentDTO, document);
        Document oldVersion = documentsDAO.findAll().stream()
                .filter(doc -> doc.getId().equals(documentDTO.getId())).findFirst()
                .orElseThrow(() -> new SQLException("There is no document with id" + document.getId()));
        document.setVerified(oldVersion.isVerified());
        documentsDAO.createNewVersionByDocument(oldVersion, document);
    }
}

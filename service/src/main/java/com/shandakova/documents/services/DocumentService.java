package com.shandakova.documents.services;

import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.entities.Document;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DocumentService {
    private DocumentsDAO documentsDAO;

    public DocumentService(DocumentsDAO documentsDAO) {
        this.documentsDAO = documentsDAO;
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

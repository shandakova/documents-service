package com.shandakova.documents.services.impl;

import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.services.DocumentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentsDAO documentsDAO;

    public DocumentServiceImpl(@Qualifier("documentsDaoJpaImpl") DocumentsDAO documentsDAO) {
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
        document.setParent(documentDTO.getParent());
        document.setVersionNumber(documentDTO.getVersionNumber());
    }

    public void createNewVersion(DocumentDTO documentDTO) throws SQLException {
        Document document = new Document();
        fillDocumentByDocumentDTO(documentDTO, document);
        Document oldVersion = documentsDAO.findAll().stream()
                .filter(doc -> doc.getId().equals(documentDTO.getId())).findFirst()
                .orElseThrow(() -> new SQLException("There is no document with id" + document.getId()));
        document.setVerified(oldVersion.isVerified());
        documentsDAO.createNewVersionByDocument(oldVersion.getId(), document);
    }
}

package com.shandakova.documents.services.impl;

import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentsRepository;
import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.services.DocumentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentsDAO documentsDAO;
    private DocumentsRepository documentsRepository;

    public DocumentServiceImpl(@Qualifier("documentsDaoJpaImpl") DocumentsDAO documentsDAO,
                               DocumentsRepository documentsRepository) {
        this.documentsDAO = documentsDAO;
        this.documentsRepository = documentsRepository;
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
        document.setType(documentDTO.getType());
        if (documentDTO.getParentDocument() != null) {
            document.setPreviousVersion(documentsRepository.findById(documentDTO.getParentDocument()).get());
        }
        document.setVersionNumber(documentDTO.getVersionNumber());
    }

    public void createNewVersion(DocumentDTO documentDTO) throws SQLException {
        Document document = new Document();
        fillDocumentByDocumentDTO(documentDTO, document);
        Document oldVersion = documentsDAO.findAll().stream()
                .filter(doc -> doc.getId().equals(documentDTO.getId())).findFirst()
                .orElseThrow(() -> new SQLException("There is no document with id" + document.getId()));
        document.setVerified(oldVersion.isVerified());
        Document newVersionByDocument = documentsDAO.createNewVersionByDocument(oldVersion.getId(), document);
    }


    public List<DocumentDTO> getAllVersionOfDocumentWithId(Integer id) throws SQLException {
        List<DocumentDTO> documents = new ArrayList<>();
        documentsDAO.getVersionByDocumentId(id).forEach(doc -> {
            DocumentDTO d = createDocumentDTOByDocument(doc);
            documents.add(d);
        });
        return documents;
    }

    private DocumentDTO createDocumentDTOByDocument(Document doc) {
        DocumentDTO d = new DocumentDTO();
        d.setName(doc.getName());
        d.setId(doc.getId());
        d.setVersionNumber(doc.getVersionNumber());
        d.setCreationDateTime(doc.getCreationDateTime());
        d.setType(doc.getType());
        d.setDescription(doc.getDescription());
        d.setImportance(doc.getImportance());
        if (doc.getParent() != null) {
            d.setParentId(doc.getParent().getId());
        }
        if (doc.getPreviousVersion() != null) {
            d.setParentDocument(doc.getPreviousVersion().getId());
        }
        return d;
    }
}

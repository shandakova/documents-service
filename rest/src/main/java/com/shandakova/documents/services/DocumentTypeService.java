package com.shandakova.documents.services;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.implementation.DocumentTypeDAOImpl;
import com.shandakova.documents.dao.interfaces.DocumentTypeDAO;
import com.shandakova.documents.dto.DocumentTypeDTO;
import com.shandakova.documents.entities.DocumentType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentTypeService {
    private static DocumentTypeDAO documentTypeDAO;

    private DocumentTypeService() {
    }

    public static DocumentTypeService getInstance(String properties) throws SQLException, IOException {
        documentTypeDAO = new DocumentTypeDAOImpl(ConnectionPool.getInstanceByProperties(properties));
        return new DocumentTypeService();
    }

    public List<DocumentTypeDTO> getAll() throws SQLException {
        List<DocumentTypeDTO> types = new ArrayList<>();
        List<DocumentType> all = documentTypeDAO.getAll();
        all.forEach(documentType -> types.add(castTypesToDTO(documentType)));
        return types;
    }

    private DocumentTypeDTO castTypesToDTO(DocumentType documentType) {
        return new DocumentTypeDTO(documentType.getId(), documentType.getName());
    }
}

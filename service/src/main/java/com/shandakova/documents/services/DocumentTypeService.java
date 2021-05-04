package com.shandakova.documents.services;

import com.shandakova.documents.dao.DocumentTypeDAO;
import com.shandakova.documents.dto.DocumentTypeDTO;
import com.shandakova.documents.entities.DocumentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentTypeService {

    private DocumentTypeDAO documentTypeDAO;

    public DocumentTypeService(@Qualifier("documentTypeDaoJpaImpl") DocumentTypeDAO documentTypeDAO) {
        this.documentTypeDAO = documentTypeDAO;
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

package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DocumentTypeDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentTypeRepository;
import com.shandakova.documents.entities.DocumentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository("documentTypeDaoJpaImpl")
public class DocumentTypeDAOJpaImpl implements DocumentTypeDAO {
    private DocumentTypeRepository repository;

    public DocumentTypeDAOJpaImpl(DocumentTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DocumentType> getAll() {
        return repository.findAll();
    }
}

package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentsRepository;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.enums.Importance;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository("documentsDaoJpaImpl")
public class DocumentsDAOJpaImpl implements DocumentsDAO {
    private DocumentsRepository documentsRepository;

    public DocumentsDAOJpaImpl(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    @Override
    public List<Document> findAllDocumentsByParentId(Integer id, boolean isDescOrder) {
        return isDescOrder
                ? documentsRepository.findByParentIdOrderByCreationDateTimeDesc(id)
                : documentsRepository.findByParentIdOrderByCreationDateTimeAsc(id);
    }

    @Override
    public void createNewDocument(Document document) {
        document.setCreationDateTime(LocalDateTime.now());
        document.setAvailable(true);
        document.setVersionNumber(0);
        if (document.getImportance() == null) {
            document.setImportance(Importance.low);
        }
        documentsRepository.save(document);
    }

    @Override
    public void createNewVersionByDocument(Document oldVersion, Document newVersion) {
        newVersion.setPreviousVersionId(oldVersion.getId());
        newVersion.setVersionNumber(oldVersion.getVersionNumber() + 1);
        newVersion.setCreationDateTime(LocalDateTime.now());
        newVersion.setAvailable(true);
        newVersion.setId(null);
        if (newVersion.getImportance() == null) {
            newVersion.setImportance(Importance.low);
        }
        documentsRepository.save(newVersion);
    }

    @Override
    public List<Document> findAll() {
        return documentsRepository.findAll();
    }

    @Override
    public void deleteAll() throws SQLException {
        documentsRepository.deleteAll();
    }
}

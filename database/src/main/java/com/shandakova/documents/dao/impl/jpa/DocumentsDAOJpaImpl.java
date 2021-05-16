package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentsRepository;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.enums.Importance;
import com.shandakova.documents.entities.enums.NodeType;
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
    public Document createNewDocument(Document document) {
        document.setCreationDateTime(LocalDateTime.now());
        document.setAvailable(true);
        document.setVersionNumber(0);
        document.setNodeType(NodeType.Values.DOCUMENT);
        if (document.getImportance() == null) {
            document.setImportance(Importance.LOW);
        }
        if (document.getDescription() == null) {
            document.setDescription("");
        }
        return documentsRepository.save(document);
    }

    @Override
    public Document createNewVersionByDocument(Integer oldVersionId, Document newVersion) throws SQLException {
        Document parent = documentsRepository.findById(oldVersionId).orElseThrow(() ->
                new SQLException("No old version with id" + oldVersionId));
        newVersion.setPreviousVersion(parent);
        int numVersion = documentsRepository.findByPreviousVersion(parent).size();
        newVersion.setVersionNumber(numVersion + 1);
        newVersion.setCreationDateTime(LocalDateTime.now());
        newVersion.setAvailable(true);
        newVersion.setNodeType(NodeType.Values.DOCUMENT);
        if (newVersion.getDescription() == null) {
            newVersion.setDescription("");
        }
        newVersion.setId(null);
        if (newVersion.getImportance() == null) {
            newVersion.setImportance(Importance.LOW);
        }
        return documentsRepository.save(newVersion);
    }

    @Override
    public List<Document> findAll() {
        return documentsRepository.findAll();
    }

    @Override
    public void deleteAll() throws SQLException {
        documentsRepository.deleteAll();
    }

    @Override
    public List<Document> getVersionByDocumentId(Integer id) throws SQLException {
        Document parent = documentsRepository.findById(id).orElseThrow(() -> new SQLException("No such document"));
        return documentsRepository.findByPreviousVersion(parent);
    }
}

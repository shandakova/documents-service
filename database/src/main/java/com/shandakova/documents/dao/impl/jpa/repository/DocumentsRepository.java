package com.shandakova.documents.dao.impl.jpa.repository;

import com.shandakova.documents.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentsRepository extends JpaRepository<Document, Integer> {
    List<Document> findByParentIdOrderByCreationDateTimeAsc(Integer id);

    List<Document> findByParentIdOrderByCreationDateTimeDesc(Integer id);
}

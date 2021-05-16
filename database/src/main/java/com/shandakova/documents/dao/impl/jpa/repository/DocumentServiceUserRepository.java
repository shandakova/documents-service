package com.shandakova.documents.dao.impl.jpa.repository;

import com.shandakova.documents.entities.DocumentServiceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentServiceUserRepository extends JpaRepository<DocumentServiceUser, Integer> {
    DocumentServiceUser findByLogin(String login);
}

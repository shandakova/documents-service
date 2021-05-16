package com.shandakova.documents.dao;

import com.shandakova.documents.entities.DocumentServiceUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface DocumentServiceUserDAO extends UserDetailsService {
    DocumentServiceUser findByLogin(String login);

    List<DocumentServiceUser> findAllUsers();

    void saveUser(DocumentServiceUser user);

    void deleteUserById(Integer id);

    void deleteUserByLogin(String login);

}

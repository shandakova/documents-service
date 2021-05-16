package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DocumentServiceUserDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DocumentServiceUserRepository;
import com.shandakova.documents.entities.DocumentServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("documentServiceUserDAOJpaImpl")
public class DocumentServiceUserDAOJpaImpl implements DocumentServiceUserDAO {
    @Autowired
    private DocumentServiceUserRepository documentServiceUserRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        DocumentServiceUser user = documentServiceUserRepository.findByLogin(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not authorized.");
        }
        return User.withUsername(user.getLogin())
                .password(user.getPasswordHash())
                .authorities(user.getRole().name()).build();
    }

    @Override
    public DocumentServiceUser findByLogin(String login) {
        return documentServiceUserRepository.findByLogin(login);
    }

    @Override
    public List<DocumentServiceUser> findAllUsers() {
        return documentServiceUserRepository.findAll();
    }

    @Override
    public void saveUser(DocumentServiceUser user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        documentServiceUserRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        documentServiceUserRepository.deleteById(id);
    }

    @Override
    public void deleteUserByLogin(String login) {
        DocumentServiceUser user = documentServiceUserRepository.findByLogin(login);
        deleteUserById(user.getId());
    }
}

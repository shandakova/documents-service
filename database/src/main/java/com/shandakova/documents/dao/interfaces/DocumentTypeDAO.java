package com.shandakova.documents.dao.interfaces;

import com.shandakova.documents.entities.DocumentType;

import java.sql.SQLException;
import java.util.List;

public interface DocumentTypeDAO {
    List<DocumentType> getAll() throws SQLException;
}

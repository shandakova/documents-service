package com.shandakova.documents.services;

import com.shandakova.documents.dto.DocumentTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface DocumentTypeService {
    List<DocumentTypeDTO> getAll() throws SQLException;
}

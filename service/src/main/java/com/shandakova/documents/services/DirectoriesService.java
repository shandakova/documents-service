package com.shandakova.documents.services;

import com.shandakova.documents.dto.DirectoryDTO;

import java.sql.SQLException;
import java.util.List;

public interface DirectoriesService {
    void create(DirectoryDTO directoryDTO) throws SQLException;

    void createMany(List<DirectoryDTO> directoriesDTO) throws SQLException;

    void update(DirectoryDTO directoryDTO) throws SQLException;

}

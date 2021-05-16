package com.shandakova.documents.dao;

import com.shandakova.documents.entities.Directory;

import java.sql.SQLException;
import java.util.List;

public interface DirectoriesDAO {
    Directory create(Directory directory) throws SQLException;

    List<Directory> createMany(List<Directory> directoryList) throws SQLException;

    void updateByID(Directory directory) throws SQLException;

    Directory findById(Integer id) throws SQLException;

    List<Directory> findAllDirectories() throws SQLException;

    List<Directory> findAllDirectoriesByParentId(Integer id, boolean isDescOrder) throws SQLException;

    void deleteAll() throws SQLException;
}

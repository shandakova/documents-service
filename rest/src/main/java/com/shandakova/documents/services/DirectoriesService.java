package com.shandakova.documents.services;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.implementation.DirectoriesDAOImpl;
import com.shandakova.documents.dao.interfaces.DirectoriesDAO;
import com.shandakova.documents.dto.DirectoryDTO;
import com.shandakova.documents.entities.Directory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DirectoriesService {
    private static DirectoriesDAO directoriesDAO;
    private DirectoriesService(){}

    public static DirectoriesService getInstance(String properties) throws SQLException, IOException {
        directoriesDAO = new DirectoriesDAOImpl(ConnectionPool.getInstanceByProperties(properties));
        return new DirectoriesService();
    }


    public void create(DirectoryDTO directoryDTO) throws SQLException {
        Directory directory = new Directory();
        fillDirectoryByDirectoryDTO(directoryDTO, directory);
        directoriesDAO.create(directory);
    }

    private void fillDirectoryByDirectoryDTO(DirectoryDTO directoryDTO, Directory directory) {
        directory.setId(directoryDTO.getId());
        directory.setParentId(directoryDTO.getParentId());
        directory.setName(directoryDTO.getName());
    }

    public void createMany(List<DirectoryDTO> directoriesDTO) throws SQLException {
        List<Directory> directories = new ArrayList<>();
        directoriesDTO.forEach(directoryDTO -> {
            Directory directory = new Directory();
            fillDirectoryByDirectoryDTO(directoryDTO, directory);
            directories.add(directory);
        });
        directoriesDAO.createMany(directories);
    }

    public void update(DirectoryDTO directoryDTO) throws SQLException {
        Directory directory = new Directory();
        fillDirectoryByDirectoryDTO(directoryDTO, directory);
        directoriesDAO.updateByID(directory);
    }
}

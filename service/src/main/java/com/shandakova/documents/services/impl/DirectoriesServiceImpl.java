package com.shandakova.documents.services.impl;

import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dto.DirectoryDTO;
import com.shandakova.documents.entities.Directory;
import com.shandakova.documents.services.DirectoriesService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoriesServiceImpl implements DirectoriesService {

    private DirectoriesDAO directoriesDAO;

    public DirectoriesServiceImpl(@Qualifier("directoriesDaoJpaImpl") DirectoriesDAO directoriesDAO) {
        this.directoriesDAO = directoriesDAO;
    }

    public void create(DirectoryDTO directoryDTO) throws SQLException {
        Directory directory = new Directory();
        fillDirectoryByDirectoryDTO(directoryDTO, directory);
        Directory dir = directoriesDAO.create(directory);
    }

    private void fillDirectoryByDirectoryDTO(DirectoryDTO directoryDTO, Directory directory) throws SQLException {
        directory.setId(directoryDTO.getId());
        if (directoryDTO.getParentId() != null) {
            directory.setParent(directoriesDAO.findById(directoryDTO.getId()));
        } else {
            directory.setParent(null);
        }
        directory.setName(directoryDTO.getName());
    }

    public void createMany(List<DirectoryDTO> directoriesDTO) throws SQLException {
        List<Directory> directories = new ArrayList<>();
        for (DirectoryDTO directoryDTO : directoriesDTO) {
            Directory directory = new Directory();
            fillDirectoryByDirectoryDTO(directoryDTO, directory);
            directories.add(directory);
        }
        directoriesDAO.createMany(directories);
    }

    public void update(DirectoryDTO directoryDTO) throws SQLException {
        Directory directory = new Directory();
        fillDirectoryByDirectoryDTO(directoryDTO, directory);
        directoriesDAO.updateByID(directory);
    }
}

package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DirectoriesRepository;
import com.shandakova.documents.entities.Directory;
import com.shandakova.documents.entities.enums.NodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository("directoriesDaoJpaImpl")
public class DirectoriesDAOJpaImpl implements DirectoriesDAO {
    private DirectoriesRepository directoriesRepository;

    public DirectoriesDAOJpaImpl(DirectoriesRepository directoriesRepository) {
        this.directoriesRepository = directoriesRepository;
    }

    @Override
    public Directory create(Directory directory) throws SQLException {
        directory.setCreationDateTime(LocalDateTime.now());
        directory.setAvailable(true);
        directory.setNodeType(NodeType.Values.DIRECTORY);
        return directoriesRepository.save(directory);
    }

    @Override
    public List<Directory> createMany(List<Directory> directoryList) {
        for (int i = 0; i < directoryList.size(); i++) {
            directoryList.get(i).setCreationDateTime(LocalDateTime.now());
            directoryList.get(i).setAvailable(true);
            directoryList.get(i).setNodeType(NodeType.Values.DIRECTORY);
        }
        return directoriesRepository.saveAll(directoryList);
    }

    @Override
    public void updateByID(Directory directory) throws SQLException {
        Directory oldDirectory = directoriesRepository.findById(directory.getId())
                .orElseThrow(() -> new SQLException("There are no directory with id" + directory.getId()));
        oldDirectory.setId(directory.getId());
        oldDirectory.setName(directory.getName());
        oldDirectory.setAvailable(directory.isAvailable());
        oldDirectory.setNodeType(NodeType.Values.DIRECTORY);
        oldDirectory.setParent(directory.getParent());
        directoriesRepository.save(oldDirectory);
    }

    @Override
    public Directory findById(Integer id) throws SQLException {
        return directoriesRepository.findById(id).orElseThrow(() ->
                new SQLException("There are no directory with id" + id)
        );
    }

    @Override
    public List<Directory> findAllDirectories() {
        return directoriesRepository.findAll();
    }

    @Override
    public List<Directory> findAllDirectoriesByParentId(Integer id, boolean isDescOrder) {
        return isDescOrder
                ? directoriesRepository.findByParentIdOrderByCreationDateTimeDesc(id)
                : directoriesRepository.findByParentIdOrderByCreationDateTimeAsc(id);
    }

    @Override
    public void deleteAll() throws SQLException {
        directoriesRepository.deleteAll();
    }
}

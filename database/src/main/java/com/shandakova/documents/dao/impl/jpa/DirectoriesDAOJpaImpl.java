package com.shandakova.documents.dao.impl.jpa;

import com.shandakova.documents.dao.DirectoriesDAO;
import com.shandakova.documents.dao.impl.jpa.repository.DirectoriesRepository;
import com.shandakova.documents.entities.Directory;
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
    public void create(Directory directory) throws SQLException {
        directory.setCreationDateTime(LocalDateTime.now());
        directory.setAvailable(true);
        directoriesRepository.save(directory);
    }

    @Override
    public void createMany(List<Directory> directoryList) {
        for (int i = 0; i < directoryList.size(); i++) {
            directoryList.get(i).setCreationDateTime(LocalDateTime.now());
            directoryList.get(i).setAvailable(true);
        }
        directoriesRepository.saveAll(directoryList);
    }

    @Override
    public void updateByID(Directory directory) throws SQLException {
        Directory oldDirectory = directoriesRepository.findById(directory.getId())
                .orElseThrow(() -> new SQLException("There are no directory with id" + directory.getId()));
        oldDirectory.setId(directory.getId());
        oldDirectory.setName(directory.getName());
        oldDirectory.setAvailable(directory.isAvailable());
        oldDirectory.setParent(directory.getParent());
        directoriesRepository.save(oldDirectory);
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

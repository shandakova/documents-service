package com.shandakova.documents.dao.impl.jpa.repository;

import com.shandakova.documents.entities.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectoriesRepository extends JpaRepository<Directory, Integer> {
    List<Directory> findByParentIdOrderByCreationDateTimeAsc(Integer id);

    List<Directory> findByParentIdOrderByCreationDateTimeDesc(Integer id);
}

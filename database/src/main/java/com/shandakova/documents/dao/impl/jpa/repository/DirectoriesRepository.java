package com.shandakova.documents.dao.impl.jpa.repository;

import com.shandakova.documents.entities.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoriesRepository extends JpaRepository<Directory, Integer> {
    List<Directory> findByParentIdOrderByCreationDateTimeAsc(Integer id);

    List<Directory> findByParentIdOrderByCreationDateTimeDesc(Integer id);
}

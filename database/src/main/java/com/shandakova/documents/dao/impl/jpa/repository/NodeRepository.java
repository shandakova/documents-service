package com.shandakova.documents.dao.impl.jpa.repository;

import com.shandakova.documents.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {
    List<Node> findByNameContains(String name);

    List<Node> findByNodeType(String type);
}

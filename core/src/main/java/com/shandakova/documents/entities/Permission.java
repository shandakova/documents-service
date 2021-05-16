package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Access;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(name = "permission_seq",
            sequenceName = "table_permissions_id_seq", allocationSize = 1)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Access access;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private DocumentServiceUser user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;
}

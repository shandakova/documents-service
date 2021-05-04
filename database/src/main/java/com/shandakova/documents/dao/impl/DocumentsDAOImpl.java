package com.shandakova.documents.dao.impl;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.DocumentsDAO;
import com.shandakova.documents.entities.Document;
import com.shandakova.documents.entities.enums.Importance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository("documentsDaoSqlImpl")
public class DocumentsDAOImpl implements DocumentsDAO {
    private final ConnectionPool connectionPool;
    private final String CREATE_NODE = "INSERT INTO nodes (name,parent_id,available,creation_datetime) " +
            "VALUES (?,?,?,NOW());";
    private final String CREATE_DOCUMENT = "INSERT INTO documents " +
            "VALUES(currval('table_nodes_id_seq'),?,?,CAST(? AS importance_type),?,?,?);";
    private final String SELECT_ALL_DOC_WITH_ORDER = "SELECT * FROM nodes n INNER JOIN documents doc ON doc.id=n.id " +
            "WHERE %s ORDER BY creation_datetime %s ;";
    private final String SELECT_ALL_DOCUMENTS = "SELECT * FROM nodes n INNER JOIN documents d ON n.id=d.id;";

    private final String DELETE_ALL_DOCUMENTS = "DELETE FROM nodes WHERE id IN (SELECT id from documents);";

    public DocumentsDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private void create(Document document, boolean isNew) throws SQLException {
        Connection connection = connectionPool.getConnection();
        connection.setAutoCommit(false);
        commitDocument(document, isNew, connection);
        connection.setAutoCommit(true);
        connectionPool.returnConnection(connection);
    }

    private void commitDocument(Document document, boolean isNew, Connection connection) throws SQLException {
        PreparedStatement insertNode = connection.prepareStatement(CREATE_NODE);
        fillNodeByDocument(document, insertNode);
        insertNode.execute();
        PreparedStatement insertDocument = connection.prepareStatement(CREATE_DOCUMENT);
        fillStatementByDocument(document, insertDocument, isNew);
        insertDocument.execute();
        connection.commit();
    }

    public List<Document> findAllDocumentsByParentId(Integer id, boolean isDescOrder) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String condition = id == null ? "parent_id IS NULL" : "parent_id=" + id;
        String order = isDescOrder ? "DESC" : "ASC";
        String statement = String.format(SELECT_ALL_DOC_WITH_ORDER, condition, order);
        PreparedStatement findDir = connection.prepareStatement(statement);
        findDir.execute();
        ResultSet resultSet = findDir.getResultSet();
        connectionPool.returnConnection(connection);
        log.info("Find all documents by parent id " + id);
        return getListDocumentsFromResultSet(resultSet);
    }

    public void createNewDocument(Document document) throws SQLException {
        create(document, true);
        log.info("Create document " + document.getName());
    }

    public void createNewVersionByDocument(Document oldVersion, Document newVersion) throws SQLException {
        newVersion.setPreviousVersionId(oldVersion.getId());
        newVersion.setVersionNumber(oldVersion.getVersionNumber() + 1);
        create(newVersion, false);
        log.info("Create new version of document " + oldVersion.getName() + " with id" + oldVersion.getId());
    }

    private void fillStatementByDocument(Document document, PreparedStatement insertDocument, boolean isNew)
            throws SQLException {
        insertDocument.setString(1, document.getDescription());
        insertDocument.setInt(2, document.getTypeId());
        if (document.getImportance() != null) {
            insertDocument.setString(3, document.getImportance().toString().toLowerCase());
        } else {
            insertDocument.setString(3, "low");
        }
        if (isNew) {
            insertDocument.setInt(4, 0);
            insertDocument.setObject(6, null);
        } else {
            insertDocument.setInt(4, document.getVersionNumber());
            insertDocument.setObject(6, document.getPreviousVersionId());
        }
        insertDocument.setBoolean(5, document.isVerified());
    }

    private void fillNodeByDocument(Document document, PreparedStatement insertNode) throws SQLException {
        insertNode.setString(1, document.getName());
        insertNode.setObject(2, document.getParentId());
        insertNode.setBoolean(3, document.isAvailable());
    }

    public List<Document> findAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(SELECT_ALL_DOCUMENTS);
        ResultSet res = statement.getResultSet();
        connectionPool.returnConnection(connection);
        log.info("Find all documents.");
        return getListDocumentsFromResultSet(res);
    }

    private List<Document> getListDocumentsFromResultSet(ResultSet res) throws SQLException {
        List<Document> documents = new ArrayList<>();
        while (res.next()) {
            Document doc = new Document();
            fillDocumentByResultSet(res, doc);
            documents.add(doc);
        }
        return documents;
    }

    private void fillDocumentByResultSet(ResultSet res, Document doc) throws SQLException {
        doc.setVersionNumber(res.getInt("version_number"));
        doc.setAvailable(res.getBoolean("available"));
        doc.setPreviousVersionId(res.getObject("previous_version_id", Integer.class));
        doc.setId(res.getInt("id"));
        doc.setName(res.getString("name"));
        doc.setTypeId(res.getInt("type_id"));
        doc.setDescription(res.getString("description"));
        doc.setParentId(res.getInt("parent_id"));
        doc.setCreationDateTime(res.getObject("creation_datetime",
                OffsetDateTime.class).toLocalDateTime());
        doc.setVerified(res.getBoolean("verified"));
        Importance importance = Importance.valueOf(res.getString("importance"));
        doc.setImportance(importance);
    }

    public void deleteAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        connection.createStatement().execute(DELETE_ALL_DOCUMENTS);
        connectionPool.returnConnection(connection);
        log.info("Delete all documents");
    }
}

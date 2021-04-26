package com.shandakova.documents.dao.implementation;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.interfaces.DirectoriesDAO;
import com.shandakova.documents.entities.Directory;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DirectoriesDAOImpl implements DirectoriesDAO {
    private final ConnectionPool connectionPool;
    private final String CREATE_NODE = "INSERT INTO nodes (name,parent_id,available,creation_datetime) " +
            "VALUES (?,?,?,NOW());";
    private final String CREATE_DIRECTORY = "INSERT INTO directories VALUES(currval('table_nodes_id_seq'));";
    private final String UPDATE_NODE = "UPDATE nodes SET (name,parent_id,available) = (?,?,?);";
    private final String SELECT_ALL_DIR_WITH_ORDER = "SELECT * FROM nodes n INNER JOIN directories dir ON dir.id=n.id " +
            "WHERE %s ORDER BY creation_datetime %s ;";
    private final String SELECT_ALL_DIRECTORY = "SELECT * FROM nodes n INNER JOIN directories d ON n.id=d.id;";
    private final String DELETE_ALL_DIRECTORY = "DELETE FROM nodes WHERE id IN (SELECT id from directories);";

    public DirectoriesDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void create(Directory directory) throws SQLException {
        Connection connection = connectionPool.getConnection();
        connection.setAutoCommit(false);
        commitDirectory(directory, connection);
        connection.setAutoCommit(true);
        connectionPool.returnConnection(connection);
        log.info("Create directory " + directory.getName());
    }

    private void commitDirectory(Directory directory, Connection connection) throws SQLException {
        PreparedStatement insertNode = connection.prepareStatement(CREATE_NODE);
        fillNodeByDirectory(directory, insertNode);
        insertNode.execute();
        PreparedStatement insertDirectories = connection.prepareStatement(CREATE_DIRECTORY);
        insertDirectories.execute();
        connection.commit();
    }

    private void fillNodeByDirectory(Directory directory, PreparedStatement insertNode) throws SQLException {
        insertNode.setString(1, directory.getName());
        insertNode.setObject(2, directory.getParentId());
        insertNode.setBoolean(3, directory.isAvailable());
    }

    public void createMany(List<Directory> directoryList) throws SQLException {
        Connection connection = connectionPool.getConnection();
        connection.setAutoCommit(false);
        List<String> names = new ArrayList<>();
        for (Directory directory : directoryList) {
            commitDirectory(directory, connection);
        }
        connection.setAutoCommit(true);
        names.forEach(name -> log.info("Create directory with name: " + name));
    }

    public void updateByID(Directory directory) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement updateNode = connection.prepareStatement(UPDATE_NODE);
        fillNodeByDirectory(directory, updateNode);
        updateNode.execute();
        connectionPool.returnConnection(connection);
        log.info("Update directory with id " + directory.getId());
    }

    public List<Directory> findAllDirectories() throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(SELECT_ALL_DIRECTORY);
        ResultSet res = statement.getResultSet();
        connectionPool.returnConnection(connection);
        log.info("Find all directories.");
        return getListDirectoriesFromResultSet(res);
    }

    public List<Directory> findAllDirectoriesByParentId(Integer id, boolean isDescOrder) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String condition = id == null ? "parent_id IS NULL" : "parent_id=" + id;
        String order = isDescOrder ? "DESC" : "ASC";
        String statement = String.format(SELECT_ALL_DIR_WITH_ORDER, condition, order);
        PreparedStatement findDir = connection.prepareStatement(statement);
        findDir.execute();
        ResultSet resultSet = findDir.getResultSet();
        connectionPool.returnConnection(connection);
        log.info("Find all directories by parent id " + id);
        return getListDirectoriesFromResultSet(resultSet);
    }

    private List<Directory> getListDirectoriesFromResultSet(ResultSet resultSet) throws SQLException {
        List<Directory> directoryList = new ArrayList<>();
        while (resultSet.next()) {
            Directory directory = getOneDirectory(resultSet);
            directoryList.add(directory);
        }
        return directoryList;
    }

    private Directory getOneDirectory(ResultSet resultSet) throws SQLException {
        Directory directory = new Directory();
        directory.setId(resultSet.getInt("id"));
        directory.setName(resultSet.getString("name"));
        directory.setAvailable(resultSet.getBoolean("available"));
        directory.setParentId(resultSet.getObject("parent_id", Integer.class));
        directory.setCreationDateTime(
                resultSet.getObject("creation_datetime", OffsetDateTime.class).toLocalDateTime());
        return directory;
    }

    public void deleteAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        connection.createStatement().execute(DELETE_ALL_DIRECTORY);
        connectionPool.returnConnection(connection);
        log.info("Delete all directories.");
    }

}

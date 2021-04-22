package com.shandakova.documents.dao;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.entities.Directory;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DirectoriesDAO {
    private final ConnectionPool connectionPool;

    public DirectoriesDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void create(Directory directory) throws SQLException {
        Connection connection = connectionPool.getConnection();
        connection.setAutoCommit(false);
        String CREATE_NODE = "INSERT INTO nodes (name,parent_id,available,creation_datetime) " +
                "VALUES (?,?,?,NOW());";
        PreparedStatement insertNode = connection.prepareStatement(CREATE_NODE);
        fillNodeByDirectory(directory, insertNode);
        insertNode.execute();
        String CREATE_DIRECTORY = "INSERT INTO directories VALUES(currval('table_nodes_id_seq'));";
        PreparedStatement insertDirectories = connection.prepareStatement(CREATE_DIRECTORY);
        insertDirectories.execute();
        connection.commit();
        connection.setAutoCommit(true);
        connectionPool.returnConnection(connection);
    }

    private void fillNodeByDirectory(Directory directory, PreparedStatement insertNode) throws SQLException {
        insertNode.setString(1, directory.getName());
        insertNode.setObject(2, directory.getParentId());
        insertNode.setBoolean(3, directory.isAvailable());
    }

    public void createMany(List<Directory> directoryList) throws SQLException {
        for (Directory directory : directoryList) {
            create(directory);
        }
    }

    public void updateByID(Directory directory) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String UPDATE_NODE = "UPDATE nodes SET (name,parent_id,available) = (?,?,?);";
        PreparedStatement updateNode = connection.prepareStatement(UPDATE_NODE);
        fillNodeByDirectory(directory, updateNode);
        updateNode.execute();
        connectionPool.returnConnection(connection);
    }

    public List<Directory> findAllDirectories() throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        String SELECT_ALL_DIRECTORY = "SELECT * FROM nodes n INNER JOIN directories d ON n.id=d.id;";
        statement.execute(SELECT_ALL_DIRECTORY);
        ResultSet res = statement.getResultSet();
        connectionPool.returnConnection(connection);
        return getListDirectoriesFromResultSet(res);
    }

    public List<Directory> findAllDirectoriesByParentId(Integer id, boolean isDescOrder) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String condition = id == null ? "parent_id IS NULL" : "parent_id=" + id;
        String order = isDescOrder ? "DESC" : "ASC";
        String SELECT_ALL_DIR_WITH_ORDER = "SELECT * FROM nodes n INNER JOIN directories dir ON dir.id=n.id " +
                "WHERE %s ORDER BY creation_datetime %s ;";
        String statement = String.format(SELECT_ALL_DIR_WITH_ORDER, condition, order);
        PreparedStatement findDir = connection.prepareStatement(statement);
        findDir.execute();
        ResultSet resultSet = findDir.getResultSet();
        connectionPool.returnConnection(connection);
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
        String DELETE_ALL_DIRECTORY = "DELETE FROM nodes WHERE id IN (SELECT id from directories);";
        connection.createStatement().execute(DELETE_ALL_DIRECTORY);
        connectionPool.returnConnection(connection);
    }

}

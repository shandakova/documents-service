package com.shandakova.documents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionPool {

    private final String url;
    private final String username;
    private final String password;
    private static final int POOL_SIZE = 20;
    private final List<Connection> connectionsPool;
    private final List<Connection> activeConnections = new ArrayList<>();

    private ConnectionPool(String url, String username, String password, List<Connection> connections) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionsPool = connections;
    }

    public Connection getConnection() throws SQLException {
        if (connectionsPool.isEmpty()) {
            throw new RuntimeException("Pool has no available connection.");
        }
        Connection connection = connectionsPool.remove(connectionsPool.size() - 1);
        int TIMEOUT = 5;
        if (!connection.isValid(TIMEOUT)) {
            connection = createConnection(url, username, password);
        }
        activeConnections.add(connection);
        return connection;
    }

    public void returnConnection(Connection connection) {
        activeConnections.remove(connection);
        connectionsPool.add(connection);
    }

    public static ConnectionPool getInstanceByProperties(String properties) throws IOException, SQLException {
        Properties prop = new Properties();
        InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(properties);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("File of database properties '" + properties + "' not found.");
        }
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        System.out.println(url + " " + password);
        ArrayList<Connection> connections = getInitialConnections(url, username, password);
        return new ConnectionPool(url, username, password, connections);
    }


    private static ArrayList<Connection> getInitialConnections(String url, String username, String password) throws SQLException {
        ArrayList<Connection> connections = new ArrayList<>();
        for (int i = 0; i < ConnectionPool.POOL_SIZE; i++) {
            connections.add(createConnection(url, username, password));
        }
        return connections;
    }

    private static Connection createConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void clear() throws SQLException {
        connectionsPool.addAll(activeConnections);
        activeConnections.clear();
        for (Connection connection : connectionsPool) {
            connection.close();
        }
        connectionsPool.clear();
    }
}

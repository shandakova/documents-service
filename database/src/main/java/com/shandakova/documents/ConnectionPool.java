package com.shandakova.documents;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ConnectionPool {

    private final String url;
    private final String username;
    private final String password;
    private static final int POOL_SIZE = 20;
    private final List<Connection> connectionsPool;
    private final List<Connection> activeConnections = Collections.synchronizedList(new ArrayList<>());

    public ConnectionPool(String url, String username, String password, List<Connection> connections) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionsPool = connections;
    }

    public synchronized Connection getConnection() throws SQLException {
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

    public synchronized void returnConnection(Connection connection) throws SQLException {
        activeConnections.remove(connection);
        connection.close();
        connectionsPool.add(createConnection(url, username, password));
    }

    public synchronized static ConnectionPool getInstanceByProperties(String properties) throws IOException, SQLException {
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
        log.info("Created connection pool to url" + url);
        List<Connection> connections = getInitialConnections(url, username, password);
        return new ConnectionPool(url, username, password, connections);
    }


    private static List<Connection> getInitialConnections(String url, String username, String password) throws SQLException {
        List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
        for (int i = 0; i < ConnectionPool.POOL_SIZE; i++) {
            connections.add(createConnection(url, username, password));
        }
        return connections;
    }

    private synchronized static Connection createConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public synchronized void clear() throws SQLException {
        connectionsPool.addAll(activeConnections);
        activeConnections.clear();
        for (Connection connection : connectionsPool) {
            connection.close();
        }
        connectionsPool.clear();
    }
}

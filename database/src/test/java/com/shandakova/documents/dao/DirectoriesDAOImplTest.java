package com.shandakova.documents.dao;

import com.shandakova.documents.ConnectionPool;
import com.shandakova.documents.dao.implementation.DirectoriesDAOImpl;
import com.shandakova.documents.dao.interfaces.DirectoriesDAO;
import com.shandakova.documents.entities.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoriesDAOImplTest {
    private DirectoriesDAO directoriesDAO;
    private ConnectionPool connectionPool;

    @Before
    public void initDAO() throws SQLException, IOException {
        connectionPool = ConnectionPool.getInstanceByProperties("database.properties");
        directoriesDAO = new DirectoriesDAOImpl(connectionPool);
    }

    @After
    public void shutdown() throws SQLException {
        directoriesDAO.deleteAll();
    }

    @Test
    public void testCreateDirectory() throws SQLException {
        directoriesDAO.deleteAll();
        List<Directory> allDirectories = directoriesDAO.findAllDirectories();
        int size = allDirectories.size();

        Directory directory = saveTestDirectory();

        allDirectories = directoriesDAO.findAllDirectories();
        assertEquals(size + 1, allDirectories.size());
        assertTrue(isDirectoryExistInList(directory, allDirectories));
    }

    @Test
    public void testUpdateById() throws SQLException {
        directoriesDAO.deleteAll();
        saveTestDirectory();
        saveTestDirectory();
        Directory directory = directoriesDAO.findAllDirectories().get(0);
        Directory directory2 = directoriesDAO.findAllDirectories().get(1);
        String newName = "new-cool-name" + LocalDateTime.now();
        directory.setName(newName);
        directory.setAvailable(false);
        directory.setParentId(directory2.getId());
        directoriesDAO.updateByID(directory);
        List<Directory> directories = directoriesDAO.findAllDirectories();
        boolean isDirectoryUpdated = directories.stream().anyMatch(d -> d.getId().equals(directory.getId())
                && d.getName().equals(newName)
                && d.getParentId().equals(directory2.getId())
                && d.getCreationDateTime().equals(directory.getCreationDateTime()));
        assertTrue(isDirectoryUpdated);

    }

    private Directory saveTestDirectory() throws SQLException {
        Directory directory = createAndFillTestDirectory();
        directoriesDAO.create(directory);
        return directory;
    }

    private Directory createAndFillTestDirectory() {
        Directory directory = new Directory();
        directory.setName("test-directory" + LocalDateTime.now());
        directory.setParentId(null);
        directory.setAvailable(true);
        return directory;
    }

    private boolean isDirectoryExistInList(Directory directory, List<Directory> directories) {
        return directories.stream().anyMatch(d -> d.getName().equals(directory.getName()) &&
                Objects.equals(d.getParentId(), directory.getParentId()));
    }

    @Test
    public void testCreateManyDirectories() throws SQLException {
        directoriesDAO.deleteAll();
        List<Directory> directories = new ArrayList<>();
        int numberCreatedDirectories = 5;
        for (int i = 0; i < numberCreatedDirectories; i++) {
            directories.add(createAndFillTestDirectory());
        }
        directoriesDAO.createMany(directories);
        List<Directory> directoriesFromDB = directoriesDAO.findAllDirectories();
        assertEquals(numberCreatedDirectories, directoriesFromDB.size());
    }

    @Test
    public void testFindAllDirectories() throws SQLException {
        List<Directory> directories = directoriesDAO.findAllDirectories();
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM nodes n INNER JOIN directories d ON n.id=d.id;");
        ResultSet set = statement.getResultSet();
        int expectedSize = 0;
        while (set.next()) {
            expectedSize++;
            assertTrue(checkFieldsByID(set, directories));
        }
        assertEquals(expectedSize, directories.size());
        connectionPool.returnConnection(connection);
    }

    private boolean checkFieldsByID(ResultSet set, List<Directory> directories) throws SQLException {
        int id = set.getInt("id");
        Directory directory = findByIdInList(directories, id);
        boolean flag = true;
        flag = directory.getName().equals(set.getString("name")) &&
                directory.getParentId().equals(set.getObject("parent_id", Integer.class)) &&
                directory.getCreationDateTime().equals(set.getObject("creation_datetime", OffsetDateTime.class)
                        .toLocalDateTime());
        return flag;
    }

    private Directory findByIdInList(List<Directory> directories, int id) {
        return directories.stream().filter(directory -> directory.getId() == id)
                .findFirst().orElseThrow(() -> new RuntimeException("There are no directories with id:" + id));
    }

    @Test
    public void testDeleteAll() throws SQLException {
        for (int i = 0; i < 5; i++) {
            testCreateDirectory();
        }
        int size = directoriesDAO.findAllDirectories().size();
        assertTrue(size > 0);
        directoriesDAO.deleteAll();
        size = directoriesDAO.findAllDirectories().size();
        assertEquals(0, size);
    }

    @Test
    public void testFindAllDirectoriesByParentIdDesc() throws SQLException {
        int dirNumber = 10;
        for (int i = 0; i < dirNumber; i++) {
            directoriesDAO.create(createAndFillTestDirectory());
        }
        List<Directory> directories = directoriesDAO.findAllDirectoriesByParentId(null, true);
        assertEquals(dirNumber, directories.size());
        for (int i = 0; i < dirNumber - 1; i++) {
            LocalDateTime curr = directories.get(i).getCreationDateTime();
            LocalDateTime next = directories.get(i + 1).getCreationDateTime();
            assertTrue(curr.isAfter(next));
        }
    }
}
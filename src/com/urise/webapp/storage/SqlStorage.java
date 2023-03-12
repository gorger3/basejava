package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private ConnectionFactory connectionFactory;

    SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }

    }

    @Override
    public void update(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("update resume set full_name = ? where uuid = ?")) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("insert into resume (uuid, full_name) values (?, ?)")) {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.executeQuery();
        } catch (SQLException e) {
            throw new StorageException(e);
        }

    }

    @Override
    public Resume get(String uuid) {
        String query = "select * from resume r where r.uuid = ?";
//        ResultSet rs = executePreparedStatement(query, ps -> {
//            ps.setString(1, uuid);
//            ResultSet resultSet = ps.executeQuery();
//            if (!resultSet.next()) {
//                throw new NotExistStorageException(uuid);
//            }
//            Resume r = new Resume(uuid, resultSet.getString("full_name"));
//
//            return resultSet;

//            ResultSet rs = ps.executeQuery();
//            if (!rs.next()) {
//                throw new NotExistStorageException(uuid);
//            }
//            Resume r = new Resume(uuid, rs.getString("full_name"));
//        });
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            return r;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from resume where uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select * from resume order by uuid")) {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return list;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select count(*) from resume")) {
            ResultSet rs = ps.executeQuery();
            rs.last();
            return rs.getRow();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

//    private ResultSet executePreparedStatement(String query, SqlHelper helper) {
//        try (Connection conn = connectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
//            return helper.execute(ps);
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }
//
//    @FunctionalInterface
//    interface SqlHelper {
//        ResultSet execute(PreparedStatement ps) throws SQLException;
//    }

}


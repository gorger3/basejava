package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        String query = "TRUNCATE resume RESTART IDENTITY CASCADE";
        executePreparedStatement(query, ps -> ps.execute());
    }

    @Override
    public void update(Resume r) {
        String query = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        executePreparedStatement(query, ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r);
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        String query = "INSERT INTO resume (uuid, full_name) VALUES (?, ?)";
        executePreparedStatement(query, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String query = "SELECT * FROM resume r WHERE r.uuid = ?";
        return executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });

    }

    @Override
    public void delete(String uuid) {
        String query = "DELETE FROM resume WHERE uuid = ?";
        executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        String query = "SELECT * FROM resume ORDER BY full_name";
        return executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume((rs.getString("uuid")),
                        (rs.getString("full_name"))));
            }
            return list;
        });
    }

    @Override
    public int size() {
        String query = "SELECT COUNT(*) FROM resume";
        return executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private <T> T executePreparedStatement(String query, SqlHelper<T> helper) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return helper.execute(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(e);
            } else {
                throw new StorageException(e);
            }
        }
    }

    @FunctionalInterface
    interface SqlHelper<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

}


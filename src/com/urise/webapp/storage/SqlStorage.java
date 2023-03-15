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
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        String query = "truncate resume restart identity cascade";
        executePreparedStatement(query, ps -> ps.execute());
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("truncate resume restart identity cascade")) {
//            ps.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public void update(Resume r) {
        String query = "update resume set full_name = ? where uuid = ?";
        executePreparedStatement(query, ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            ps.execute();
            return null;
        });
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("update resume set full_name = ? where uuid = ?")) {
//            ps.setString(1, r.getFullName());
//            ps.setString(2, r.getUuid());
//            ps.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public void save(Resume r) {
        String query = "insert into resume (uuid, full_name) values (?, ?)";
        executePreparedStatement(query, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("insert into resume (uuid, full_name) values (?, ?)")) {
//            ps.setString(1, r.getUuid());
//            ps.setString(2, r.getFullName());
//            ps.execute();
//        } catch (SQLException e) {
//            throw new ExistStorageException(e);
//        }
    }

    @Override
    public Resume get(String uuid) {
        String query = "select * from resume r where r.uuid = ?";
        return executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });

//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("select * from resume r where r.uuid = ?")) {
//            ps.setString(1, uuid);
//            ResultSet rs = ps.executeQuery();
//            if (!rs.next()) {
//                throw new NotExistStorageException(uuid);
//            }
//            Resume r = new Resume(uuid, rs.getString("full_name"));
//            return r;
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public void delete(String uuid) {
        String query = "delete from resume where uuid = ?";
        executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            ps.execute();
            return null;
        });
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("delete from resume where uuid = ?")) {
//            ps.setString(1, uuid);
//            ps.execute();
//        } catch (SQLException e) {
//            throw new NotExistStorageException(e);
//        }
    }

    @Override
    public List<Resume> getAllSorted() {
        String query = "select * from resume order by uuid";
        return executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume((rs.getString("uuid")).trim(),
                        (rs.getString("full_name")).trim())); // получает строки из ResultSet и удаляет в них пробелы
            }                                                            // иначе при сравнении в тесте строки стчитаются разными
            return list;
        });
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("select * from resume order by uuid")) {
//            ResultSet rs = ps.executeQuery();
//            List<Resume> list = new ArrayList<>();
//            while (rs.next()) {
//                list.add(new Resume((rs.getString("uuid")).trim(),
//                        (rs.getString("full_name")).trim())); // получает строки из ResultSet и удаляет в них пробелы
//            }                                                            // иначе при сравнении в тесте строки стчитаются разными
//            return list;
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public int size() {
        String query = "select count(*) from resume";
        return executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("select count(*) from resume")) {
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    private <T> T executePreparedStatement(String query, SqlHelper<T> helper) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             return helper.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    interface SqlHelper<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

}


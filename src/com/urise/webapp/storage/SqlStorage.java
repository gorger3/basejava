package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    SqlHelper sqlHelper;


    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executePreparedStatement("TRUNCATE resume RESTART IDENTITY CASCADE", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        String uuid = r.getUuid();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ps.execute();
            }
            insertContacts(r, conn);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(r, conn);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String query = "SELECT * FROM resume r " + "LEFT JOIN contact c " + "ON r.uuid = c.resume_uuid " + "WHERE r.uuid = ?";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContactsFromResultSetToResume(rs, r);
            } while (rs.next());
            return r;
        });

    }

    @Override
    public void delete(String uuid) {
        //language=PostgreSQL
        String query = "DELETE FROM resume WHERE uuid = ?";
        sqlHelper.executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        //language=PostgreSQL
        String query = "SELECT * FROM resume r " + "LEFT JOIN contact c " + "ON r.uuid = c.resume_uuid " + "ORDER BY r.full_name, r.uuid";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> map = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume r;
                if (map.containsKey(uuid)) { // если резюме с данным uuid уже есть в карте, дастаёт его
                    r = map.get(uuid);
                } else { // если резюме с данным uuid ещё нет в карте, создаёт его и добавляет в карту
                    r = new Resume(uuid, rs.getString("full_name"));
                    map.put(uuid, r);
                }
                addContactsFromResultSetToResume(rs, r);
            }
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        //language=PostgreSQL
        String query = "SELECT COUNT(*) FROM resume";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void addContactsFromResultSetToResume(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            String value = rs.getString("value");
            ContactType contactType = ContactType.valueOf(type);
            r.addContact(contactType, value);
        }
    }

    private void addContactsToPreparedStatement(PreparedStatement ps, String contactValue, String contactType, String uuid) throws SQLException {
        ps.setString(1, contactValue);
        ps.setString(2, contactType);
        ps.setString(3, uuid);
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                addContactsToPreparedStatement(ps, e.getValue(), e.getKey().name(), r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
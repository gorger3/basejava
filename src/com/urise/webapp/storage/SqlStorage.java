package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    final String DRIVER = "org.postgresql.Driver";
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executePreparedStatement("TRUNCATE resume RESTART IDENTITY CASCADE", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r);
                }
            }
            deleteContactsFromTable(conn, r);
            deleteSectionsFromTable(conn, r);
            insertContactsIntoTable(conn, r);
            insertSectionsIntoTable(conn, r);
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
            insertContactsIntoTable(conn, r);
            insertSectionsIntoTable(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String query = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " + "ON r.uuid = c.resume_uuid " +
                "LEFT JOIN section s " + "ON r.uuid = s.resume_uuid " +
                "WHERE r.uuid = ?";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContactToResume(rs, r);
                addSectionToResume(rs, r);
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
        String query = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " + "ON r.uuid = c.resume_uuid " +
                "LEFT JOIN section s " + "ON r.uuid = s.resume_uuid " +
                "ORDER BY r.full_name, r.uuid";
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
                addContactToResume(rs, r);
                addSectionToResume(rs, r);
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

    private void deleteContactsFromTable(Connection conn, Resume r) throws SQLException {
        deleteAttributesFromTable(conn, r, "DELETE FROM contact WHERE resume_uuid = ?");
    }

    private void deleteSectionsFromTable(Connection conn, Resume r) throws SQLException {
        deleteAttributesFromTable(conn, r, "DELETE FROM section WHERE resume_uuid = ?");
    }

    private void deleteAttributesFromTable(Connection conn, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void addContactToResume(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSectionToResume(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("content");
        if (content != null) {
            SectionType sectionType = SectionType.valueOf(rs.getString("section_type"));
            Section section = JsonParser.read(content, Section.class);
            r.addSection(sectionType, section);
        }
    }

    private void insertContactsIntoTable(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSectionsIntoTable(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (content, section_type, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, JsonParser.write(e.getValue(), Section.class));
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
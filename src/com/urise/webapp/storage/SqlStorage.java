package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;


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
                addContacts(rs, r);
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
        return sqlHelper.transactionalExecute(conn -> {
            PreparedStatement psForResumes = conn.prepareStatement("SELECT * FROM resume ORDER BY uuid");
            List<Resume> resumes = new ArrayList<>();
            ResultSet resumesRS = psForResumes.executeQuery();
            PreparedStatement psForContacts = conn.prepareStatement("SELECT * FROM contact ORDER BY resume_uuid");
            ResultSet rsForContacts = psForContacts.executeQuery();
            while (resumesRS.next()) {
                Resume r = new Resume(resumesRS.getString("uuid"), resumesRS.getString("full_name"));
                resumes.add(r);
            }
            Map<String, Map<ContactType, String>> mapResumeContacts = new LinkedHashMap<>();
            Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
            while (rsForContacts.next()) {
                String uuid = rsForContacts.getString("resume_uuid");
                ContactType type = ContactType.valueOf(rsForContacts.getString("type"));
                String value = rsForContacts.getString("value");
                if (!mapResumeContacts.containsKey(uuid)) {
                    contacts = new EnumMap<>(ContactType.class);
                    contacts.put(type, value);
                } else {
                    contacts.put(type, value);
                }
                mapResumeContacts.put(uuid, contacts);
            }
            for (Resume r : resumes) {
                if (mapResumeContacts.containsKey(r.getUuid())) {
                    for (Map.Entry<ContactType, String> c : mapResumeContacts.get(r.getUuid()).entrySet()) {
                        r.addContact(c.getKey(), c.getValue());
                    }
                }
            }
            return resumes.stream().sorted().collect(Collectors.toList());
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

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            String value = rs.getString("value");
            ContactType contactType = ContactType.valueOf(type);
            r.addContact(contactType, value);
        }
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
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
}
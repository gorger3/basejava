package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ps.execute();
            }
            insertContacts(r, conn);
            insertSections(r, conn);
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
            insertSections(r, conn);
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
                addContacts(rs, r);
                addSections(rs, r);
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
            PreparedStatement psForResumes = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid");
            Map<String, Resume> resumes = new LinkedHashMap<>();
            ResultSet resumesRS = psForResumes.executeQuery();
            while (resumesRS.next()) {
                Resume r = new Resume(resumesRS.getString("uuid"), resumesRS.getString("full_name"));
                resumes.put(r.getUuid(), r);
            }

            PreparedStatement psForContacts = conn.prepareStatement("SELECT * FROM contact ORDER BY resume_uuid");
            Map<String, Map<ContactType, String>> mapResumeContacts = new LinkedHashMap<>();
            ResultSet contactsRS = psForContacts.executeQuery();
            Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
            while (contactsRS.next()) {
                String uuid = contactsRS.getString("resume_uuid");
                ContactType type = ContactType.valueOf(contactsRS.getString("type"));
                String value = contactsRS.getString("value");
                if (!mapResumeContacts.containsKey(uuid)) {
                    contacts = new EnumMap<>(ContactType.class);
                    contacts.put(type, value);
                } else {
                    contacts.put(type, value);
                }
                mapResumeContacts.put(uuid, contacts);
            }

            PreparedStatement psForSections = conn.prepareStatement("SELECT * FROM section ORDER BY resume_uuid");
            Map<String, Map<SectionType, Section>> mapResumeSections = new LinkedHashMap<>();
            ResultSet sectionsRS = psForSections.executeQuery();
            Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
            while (sectionsRS.next()) {
                String uuid = sectionsRS.getString("resume_uuid");
                SectionType type = SectionType.valueOf(sectionsRS.getString("section_type"));
                String value = sectionsRS.getString("section_value");
                if (!mapResumeSections.containsKey(uuid)) {
                    sections = new EnumMap<>(SectionType.class);
                    fillSections(sections, type, value);
                } else {
                    fillSections(sections, type, value);
                }
                mapResumeSections.put(uuid, sections);
            }
            for (Resume r : resumes.values()) {
                if (mapResumeContacts.containsKey(r.getUuid())) {
                    for (Map.Entry<ContactType, String> c : mapResumeContacts.get(r.getUuid()).entrySet()) {
                        r.addContact(c.getKey(), c.getValue());
                    }
                }
                if (mapResumeSections.containsKey(r.getUuid())) {
                    for (Map.Entry<SectionType, Section> s : mapResumeSections.get(r.getUuid()).entrySet()) {
                        r.addSection(s.getKey(), s.getValue());
                    }
                }
            }
            return new ArrayList<>(resumes.values());
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

    private void fillSections(Map<SectionType, Section> sections, SectionType type, String value) {
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                sections.put(type, new TextSection(value));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                String[] arrFromValue = value.split("\n");
                sections.put(type, new ListSection(Arrays.asList(arrFromValue)));
                break;
        }
    }

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            String value = rs.getString("value");
            ContactType contactType = ContactType.valueOf(type);
            r.addContact(contactType, value);
        }
    }

    private void addSections(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("section_type");
        if (type != null) {
            String value = rs.getString("section_value");
            SectionType sectionType = SectionType.valueOf(type);
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    r.addSection(sectionType, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    String[] arrFromValue = value.split("\n");
                    r.addSection(sectionType, new ListSection(Arrays.asList(arrFromValue)));
                    break;
            }
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
    private void insertSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (section_value, section_type, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                SectionType type = e.getKey();
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        ps.setString(1, e.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = (ListSection) e.getValue();
                        String delim = "\n";
                        ps.setString(1, String.join(delim, listSection.getItems()));
                        break;
                }
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
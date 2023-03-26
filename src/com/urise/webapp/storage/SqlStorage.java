package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {

    SqlHelper sqlHelper;


    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        String query = "TRUNCATE resume RESTART IDENTITY CASCADE";
        sqlHelper.executePreparedStatement(query, PreparedStatement::execute);
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
            if (r.getContacts().entrySet().size() == 0) { // если у резюме с обновлениями нет контактов, удаляет старые контакты
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                    ps.setString(1, uuid);
                    ps.execute();
                }
            } else { // если у резюме с обновлениями есть контакты
                Map<ContactType, String> contactsFromResume = r.getContacts();
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?")) { // достаёт старые контакты из таблицы
                    List<ContactType> listOfUpdatedContacts = new ArrayList<>();
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        ContactType type = ContactType.valueOf(rs.getString("type"));
                        if (contactsFromResume.containsKey(type)) { // если контакту в обновлённом резюме есть соответствие в таблице старых контактов
                            try (PreparedStatement psUpd = conn.prepareStatement("UPDATE mydb.public.contact SET value = ? WHERE type = ? AND resume_uuid = ?")) {
                                psUpd.setString(1, contactsFromResume.get(type));
                                psUpd.setString(2, type.name());
                                psUpd.setString(3, uuid);
                                psUpd.execute();

                            }
                            listOfUpdatedContacts.add(type);
                        } else { // удаляет из таблицы контакты, которых нет в обновлённом резюме
                            try (PreparedStatement psDlt = conn.prepareStatement("DELETE FROM mydb.public.contact WHERE type = ? AND resume_uuid = ?")) {
                                psDlt.setString(1, type.name());
                                psDlt.setString(2, uuid);
                                psDlt.execute();
                            }
                        }
                    }
                    for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) { // если в обновлённом резюме есть контакты, которых нет в таблице, заносит их в таблицу
                        if (listOfUpdatedContacts.contains(entry.getKey())) { // если контакт из резюме уже обновлён в таблице с помощью UPDATE,
                            continue;                                         // начинает новую итерацию
                        }
                        try (PreparedStatement psIns = conn.prepareStatement("INSERT INTO mydb.public.contact (resume_uuid, type, value) VALUES (?, ?, ?)")) { // сохраняет новые контакты из резюме
                            psIns.setString(1, uuid);
                            psIns.setString(2, entry.getKey().name());
                            psIns.setString(3, entry.getValue());
                            psIns.execute();

                        }
                    }
                }
            }
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
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                            ps.setString(1, r.getUuid());
                            ps.setString(2, e.getKey().name());
                            ps.setString(3, e.getValue());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        String query = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid = ?";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                String type = rs.getString("type");
                if (type != null) {
                    String value = rs.getString("value");
                    ContactType contactType = ContactType.valueOf(type);
                    r.addContact(contactType, value);
                }
            } while (rs.next());
            return r;
        });

    }

    @Override
    public void delete(String uuid) {
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
        String query = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "ORDER BY r.full_name, r.uuid";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> map = new HashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume r;
                if (map.containsKey(uuid)) { // если резюме с данным uuid уже есть в карте, дастаёт его
                    r = map.get(uuid);
                } else { // если резюме с данным uuid ещё нет в карте, создаёт его и добавляет в карту
                    r = new Resume(uuid, rs.getString("full_name"));
                    map.put(uuid, r);
                }
                String type = rs.getString("type");
                if (type != null) {
                    String value = rs.getString("value");
                    ContactType contactType = ContactType.valueOf(type);
                    r.addContact(contactType, value);
                }
            }
            return map.values().stream().sorted().collect(Collectors.toList());
        });
    }

    @Override
    public int size() {
        String query = "SELECT COUNT(*) FROM resume";
        return sqlHelper.executePreparedStatement(query, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

}


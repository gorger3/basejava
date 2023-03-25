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
        String query = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        sqlHelper.executePreparedStatement(query, ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r);
            }
            return null;
        });
        if (r.getContacts().entrySet().size() == 0) { // если у обновлённого резюме нет контактов, удаляет старые контакты
            String queryForContacts = "DELETE FROM contact WHERE resume_uuid = ?";
            sqlHelper.executePreparedStatement(queryForContacts, ps -> {
                ps.setString(1, uuid);
                ps.executeUpdate();
                return null;
            });
        } else { // если у обновлённого резюме есть контакты
            Map<ContactType, String> mapFromResume = r.getContacts();
            String queryToGetExistingContacts = "SELECT * FROM contact WHERE resume_uuid = ?"; // достаёт старые контакты из таблицы
            List<ContactType> listOfUpdatedContacts = new ArrayList<>();
            sqlHelper.executePreparedStatement(queryToGetExistingContacts, ps -> {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ContactType type = ContactType.valueOf(rs.getString("type"));
                    if (mapFromResume.containsKey(type)) { // если контакту в обновлённом резюме есть соответствие в таблице старых контактов
                        String queryToUpdateContact = "UPDATE mydb.public.contact SET value = ? WHERE type = ? AND resume_uuid = ?";
                        sqlHelper.executePreparedStatement(queryToUpdateContact, psU -> {
                            psU.setString(1, mapFromResume.get(type));
                            psU.setString(2, type.name());
                            psU.setString(3, uuid);
                            psU.execute();
                            return null;
                        });
                        listOfUpdatedContacts.add(type);
                    } else { // если контакту в обновлённом резюме нет соответствие в таблице старых контактов
                        String queryToDeleteContact = "DELETE FROM mydb.public.contact WHERE type = ? AND resume_uuid = ?";
                        sqlHelper.executePreparedStatement(queryToDeleteContact, psD -> {
                            psD.setString(1, type.name());
                            psD.setString(2, uuid);
                            psD.execute();
                            return null;
                        });
                    }
                }
                return null;
            });
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) { // сохраняет контакты из обновлённого резюме, которых нет в таблице старых контактов
                if (listOfUpdatedContacts.contains(entry.getKey())) { // если контакт из резюме есть в списке контактов, уже обновлённых в таблице
                    continue; // начинает новую итерацию, поскольку контакт уже был обновлён с помощью UPDATE
                }
                String queryToSaveNewContact = "INSERT INTO mydb.public.contact (resume_uuid, type, value) VALUES (?, ?, ?)"; // сохраняет новые контакты из обновлённого резюме
                sqlHelper.executePreparedStatement(queryToSaveNewContact, psS -> {
                    psS.setString(1, uuid);
                    psS.setString(2, entry.getKey().name());
                    psS.setString(3, entry.getValue());
                    psS.execute();
                    return null;
                });
            }
        }
    }

    @Override
    public void save(Resume r) {
        String query = "INSERT INTO resume (uuid, full_name) VALUES (?, ?)";
        sqlHelper.executePreparedStatement(query, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
        String queryForContacts = "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)";
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            sqlHelper.executePreparedStatement(queryForContacts, ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.execute();
                return null;
            });
        }
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
                String uuidFromTable = rs.getString("uuid");
                Resume r;
                if (map.containsKey(uuidFromTable)) {
                    r = map.get(uuidFromTable);
                } else {
                    r = new Resume((rs.getString("uuid")), (rs.getString("full_name")));
                    map.put(uuidFromTable, r);
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


package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File("C:\\Users\\GGorelik\\Documents\\java-study\\javaops\\basejava\\config\\resumes.properties");
    private static final Config INSTANCE = new Config();

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final File storageDir;

    private final Storage storage;

    private Config() {
        try (FileInputStream fis = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(fis);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
            storage = new SqlStorage(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid resumes.properties" + PROPS.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}

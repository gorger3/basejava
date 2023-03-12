package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File("config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    private Properties props = new Properties();

    private File storageDir;

    private Config() {
        try (FileInputStream fis = new FileInputStream(PROPS)) {
            props.load(fis);
            storageDir = new File(props.getProperty("storage.dir"));
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
}

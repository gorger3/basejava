package com.urise.webapp.storage;

public class FileStorageTest extends AbstractStorageTest {

    private static final SerializationStrategy STRATEGY_1 = new ObjectStreamStorage();

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, STRATEGY_1));
    }
}
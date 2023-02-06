package com.urise.webapp.storage;

public class PathStorageTest extends AbstractStorageTest {

    private static final SerializationStrategy STRATEGY_1 = new ObjectStreamStorage();

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), STRATEGY_1));
    }
}
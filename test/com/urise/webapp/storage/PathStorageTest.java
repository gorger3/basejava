package com.urise.webapp.storage;

import com.urise.webapp.storage.strategy.StreamStrategy;

public class PathStorageTest extends AbstractStorageTest {

    private static final SerializationStrategy STRATEGY_1 = new StreamStrategy();

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), STRATEGY_1));
    }
}
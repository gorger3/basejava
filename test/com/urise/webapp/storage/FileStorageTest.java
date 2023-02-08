package com.urise.webapp.storage;

import com.urise.webapp.storage.strategy.SerializationStrategy;
import com.urise.webapp.storage.strategy.StreamStrategy;

public class FileStorageTest extends AbstractStorageTest {

    private static final SerializationStrategy STRATEGY_1 = new StreamStrategy();

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, STRATEGY_1));
    }
}
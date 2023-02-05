package com.urise.webapp.storage;

public class AbstractFileStorageTest extends AbstractStorageTest {

    public AbstractFileStorageTest() {
        super(new AbstractFileStorage(STORAGE_DIR));
    }
}
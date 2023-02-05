package com.urise.webapp.storage;

public class AbstractPathStorageTest extends AbstractStorageTest {

    public AbstractPathStorageTest() {
        super(new AbstractPathStorage(STORAGE_DIR.getPath()));
    }
}
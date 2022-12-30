package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int size = 0;

    public int size() {
        return size;
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract int getIndex(String uuid);
}

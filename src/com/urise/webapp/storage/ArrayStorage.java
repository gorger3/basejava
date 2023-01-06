package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void fillDeletedElement(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    protected void insertElement(Resume r, int index) {
        storage[size] = r;
    }

    @Override
    protected Integer getSearchKey(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertResume(int index, Resume r) {
        storage[size] = r;
        size++;
    }

    @Override
    protected void removeResume(int index) {
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }
}

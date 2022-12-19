package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insertResume(int index, Resume r) {
        int insertionPoint = -index - 1;
        for (int i = size; i > insertionPoint; i--) {
            storage[i] = storage[i - 1];
        }
        storage[insertionPoint] = r;
        size++;
    }

    @Override
    protected void removeResume(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
        storage[size - 1] = null;
        size--;
    }
}
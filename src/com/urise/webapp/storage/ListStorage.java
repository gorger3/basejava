package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStorage extends AbstractStorage {

    List<Resume> storage = new ArrayList<>();

    private int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        storage.clear();
        size = 0;
    }

    @Override
    public void doUpdate(Object searchKey, Resume r) {
        storage.set((Integer) searchKey, r);
    }

    @Override
    public void doSave(Object searchKey, Resume r) {
        int insertionPoint = - (int) searchKey -1;
        storage.add(insertionPoint, r);
        size++;
    }

    @Override
    public Resume doGet(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    public void doDelete(Object searchKey) {
        storage.remove((int) searchKey);
        size--;
    }

    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage.toArray(), 0, size, searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (Integer) searchKey >= 0;
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;

public class ListStorage extends AbstractStorage {

    ArrayList<Resume> storage = new ArrayList<>();

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
        insertElement(r, (Integer) searchKey);
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

    protected void insertElement(Resume r, int index) {
        int insertIdx = -index - 1;
        storage.add(insertIdx, r);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage.toArray(), 0, size, searchKey);
    }

    @Override
    protected boolean isExist(String uuid) {
        return (Integer) getSearchKey(uuid) >= 0;
    }
}

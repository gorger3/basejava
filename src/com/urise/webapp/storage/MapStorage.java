package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    protected int size = 0;

    public int size() {
        return size;
    }

    Map<String, Resume> storage = new HashMap<>();

    public void clear() {
        storage.clear();
        size = 0;
    }

    @Override
    public void doUpdate(Object searchKey, Resume r) {
        storage.put((String) searchKey, r);
    }

    @Override
    public void doSave(Object searchKey, Resume r) {
        storage.put((String) searchKey, r);
        size++;
    }

    @Override
    public Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    public void doDelete(Object searchKey) {
        storage.remove(searchKey);
        size--;
    }


    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey(searchKey);
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {

    private final Map<String, Resume> map = new HashMap<>();

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    public void doUpdate(Object searchKey, Resume r) {
        map.put((String) searchKey, r);
    }

    @Override
    public void doSave(Object searchKey, Resume r) {
        map.put((String) searchKey, r);
    }

    @Override
    public Resume doGet(Object searchKey) {
        return map.get((String) searchKey);
    }

    @Override
    public void doDelete(Object searchKey) {
        map.remove((String) searchKey);
    }


    @Override
    public List<Resume> doCopyAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return map.containsKey((String) searchKey);
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {

    private final Map<String, Resume> map = new HashMap<>();

    @Override
    protected Resume getSearchKey(Object o) {
        return (Resume) o;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return map.containsValue(searchKey);
    }

    @Override
    public void update(Resume r) {
        Object searchKey = getExistingSearchKey(r);
        doUpdate(searchKey, r);
    }

    @Override
    public void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r);
        doSave(searchKey, r);
    }

    public Resume get(Resume r) {
        Object searchKey = getExistingSearchKey(r);
        return doGet(searchKey);
    }

    public void delete(Resume r) {
        Object searchKey = getExistingSearchKey(r);
        doDelete(searchKey);
    }

    @Override
    public void doUpdate(Object searchKey, Resume r) {
        map.put(((Resume) searchKey).getUuid(), r);
    }

    @Override
    public void doSave(Object searchKey, Resume r) {
        map.put(((Resume) searchKey).getUuid(), r);
    }

    @Override
    public Resume doGet(Object searchKey) {
        return map.get(((Resume) searchKey).getUuid());
    }

    @Override
    public void doDelete(Object searchKey) {
        map.remove(((Resume) searchKey).getUuid());
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }


    public List<Resume> getAllSorted() {
        return new ArrayList<>(map.values());
    }
}

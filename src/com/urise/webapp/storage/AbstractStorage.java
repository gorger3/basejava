package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int size = 0;

    public int size() {
        return size;
    }

    public void update(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(searchKey, r);
    }

    public abstract void doUpdate(Object searchKey, Resume r);

    public void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(searchKey, r);
    }

    public abstract void doSave(Object searchKey, Resume r);

    public Resume get(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    public abstract Resume doGet(Object searchKey);

    public void delete(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    public abstract void doDelete(Object searchKey);

    protected abstract void insertElement(Resume r, int index);

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(String uuid);

    private Object getExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(uuid)) {
            return searchKey;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(uuid)) {
            return searchKey;
        } else {
            throw new ExistStorageException(uuid);
        }
    }
}

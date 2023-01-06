package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;

public abstract class AbstractStorage implements Storage {

    protected abstract Object getSearchKey(Object o);

    protected abstract boolean isExist(Object searchKey);

    public abstract void doUpdate(Object searchKey, Resume r);

    public abstract void doSave(Object searchKey, Resume r);

    public abstract Resume doGet(Object searchKey);

    public abstract void doDelete(Object searchKey);

    public void update(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(searchKey, r);
    }

    public void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(searchKey, r);
    }

    public Resume get(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    Object getExistingSearchKey(Object o) {
        Object searchKey = getSearchKey(o);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(o);
        }
        return searchKey;
    }

    Object getNotExistingSearchKey(Object o) {
        Object searchKey = getSearchKey(o);
        if (isExist(searchKey)) {
            throw new ExistStorageException(o);
        }
        return searchKey;
    }

    protected static final Comparator<Resume> RESUME_COMPARATOR = Comparator
            .comparing(Resume::getFullName)
            .thenComparing(Resume::getUuid);

}

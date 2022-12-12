package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    final private Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        String uuid = r.getUuid();
        int index = getSearchKey(uuid);
        if (index != -1) {
            storage[index] = r;
            return;
        } else {
            System.out.println("Error: there is no resume to update in the storage");
        }
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        int index = getSearchKey(uuid);
        if (index != -1) {
            System.out.println("Error: the resume with " + r + " is already in the storage.");
            return;
        }
        if (size < storage.length) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("Error: there is no place in the storage for a new resume.");
        }
    }

    public Resume get(String uuid) {
        int index = getSearchKey(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("Error: there is no resume with " + uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        int index = getSearchKey(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
            return;
        }
        System.out.println("Error: there is no resume with " + uuid);
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int getSearchKey(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (uuid == storage[i].getUuid()) {
                index = i;
                return index;
            }
        }
        return index;
    }
}

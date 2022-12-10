package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size;

    private boolean resumeInStorage(String uuid, int i) {
        if (uuid == storage[i].getUuid()) {
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        String uuid = r.getUuid();
        for (int i = 0; i < size; i++) {
            if (resumeInStorage(uuid, i)) {
                storage[i] = r;
                return;
            }
        }
        System.out.println("Error: there is no resume to update in the storage");
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        for (int i = 0; i < size; i++) {
            if (resumeInStorage(uuid, i)) {
                System.out.println("Error: the resume with " + r + " is already in the storage.");
                return;
            }
        }
        if (size < storage.length) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("Error: there is no place in the storage for a new resume.");
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (resumeInStorage(uuid, i)) return storage[i];
        }
        System.out.println("Error: there is no resume with " + uuid);
        return null;
    }

    public void delete(String uuid) {
//        int sizeBeforeDelete = size;
        for (int i = 0; i < size; i++) {
            if (resumeInStorage(uuid, i)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
                return;
            }
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
}

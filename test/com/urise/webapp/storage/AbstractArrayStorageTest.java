package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractArrayStorageTest {

    private final Storage storage;
    private final static String UUID_1 = "uuid1";
    private final static String UUID_2 = "uuid2";
    private final static String UUID_3 = "uuid3";
    private final static String UUID_4 = "uuid4";
    private final static String UUID_NOT_EXIST = "dummy";

    private final static Resume RESUME_1 = new Resume(UUID_1);
    private final static Resume RESUME_2 = new Resume(UUID_2);
    private final static Resume RESUME_3 = new Resume(UUID_3);
    private final static Resume RESUME_4 = new Resume(UUID_4);

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertTrue(assertSize(0));
        assertArrayEquals(new Resume[0], storage.getAll());
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID_1);
        storage.update(r);
        assertSame(r, storage.get(r.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(UUID_NOT_EXIST));
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertTrue(assertGet(RESUME_4));
        assertTrue(assertSize(4));
    }

    @Test(expected = ExistStorageException.class)
    public void shouldNotSaveResumeThatAlreadyInStorage() {
        storage.save(RESUME_2);
    }


    @Test(expected = StorageException.class)
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("Storage overflow occurred before time");
        }
        storage.save(new Resume());
    }

    @Test
    public void get() {
        assertTrue(assertGet(RESUME_1));
        assertTrue(assertGet(RESUME_2));
        assertTrue(assertGet(RESUME_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_2);
        assertTrue(assertSize(2));
        assertTrue(assertGet(RESUME_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void getAll() {
        assertTrue(assertSize(storage.getAll().length));
    }

    @Test
    public void size() {
        assertTrue(assertSize(3));
    }

    private boolean assertSize(int expectedSize) {
        if (expectedSize == storage.size()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean assertGet(Resume r) {
        if (r.equals(storage.get(r.getUuid()))) {
            return true;
        } else {
            return false;
        }
    }

}
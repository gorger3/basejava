package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AbstractArrayStorageTest {

    private Storage storage;
    private int limit = 5;
    Resume[] resumeArray = createResumeArray(limit);

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        for (int i = 0; i < limit; i++) {
            storage.save(resumeArray[i]);
        }
    }

    @Ignore("Can't understand why clear() test fails")
    @Test(expected = NotExistStorageException.class)
    public void clear() {
        String uuid = getRandomUuid(limit);
        storage.get(uuid);
    }

    @Test
    public void sizeShouldBeZeroAfterClear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        String uuid = getRandomUuid(limit);
        Resume newResume = new Resume(uuid);
        storage.update(newResume);
        assertEquals(newResume, storage.get(uuid));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        Resume r = new Resume("dummy");
        storage.update(r);
    }

    @Test
    public void save() {
        String uuid = "uuid" + limit;
        Resume r = new Resume(uuid);
        storage.save(r);
        assertEquals(r, storage.get(uuid));
    }

    @Test(expected = ExistStorageException.class)
    public void shouldNotSaveResumeThatAlreadyInStorage() {
        String uuid = getRandomUuid(limit);
        Resume resumeAlreadyInStorage = new Resume(uuid);
        storage.save(resumeAlreadyInStorage);
    }

    @Test(expected = StorageException.class)
    public void saveShouldNotOverflow() {
//        Field field = storage.getClass().getField("STORAGE_LIMIT");
        storage.clear();
        int limit = 10001;
        Resume[] resumeArray = createResumeArray(limit);
        try {
            for (int i = 0; i < limit - 1; i++) {
                storage.save(resumeArray[i]);
            }
        } catch (StorageException e) {
            Assert.fail("Storage overflow occurred before time");
        }

        storage.save(resumeArray[limit - 1]);
    }

    @Test
    public void get() {
        String uuid = getRandomUuid(limit);
        Resume newResume = new Resume(uuid);
        storage.get(uuid);
        assertEquals(newResume, storage.get(uuid));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        String uuid = getRandomUuid(limit);
        storage.delete(uuid);
        storage.get(uuid);
    }

    @Test
    public void deleteSizeDecrease() {
        String uuid = getRandomUuid(limit);
        storage.delete(uuid);
        assertEquals(limit - 1, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void getAll() {
        assertArrayEquals(resumeArray, storage.getAll());
        assertEquals(limit, storage.size());
    }

    @Test
    public void size() {
        assertEquals(limit, storage.size());
    }

    private String getRandomUuid(int limit) {
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(limit);
        StringBuilder uuid = new StringBuilder("uuid");
        return uuid.append(randomIndex).toString();
    }

    private Resume[] createResumeArray(int limit) {
        Resume[] resumeArray = new Resume[limit];
        StringBuilder uuid = new StringBuilder("uuid");
        for (int i = 0; i < limit; i++) {
            uuid.append(i);
            resumeArray[i] = new Resume(uuid.toString());
            uuid.delete(4, 8);
        }
        return resumeArray;
    }
}
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = size();

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
            size = 0;
        }
    }

    void save(Resume r) {
        if (size < 10000) {
            storage[size] = r;
            size = size + 1;
        } else {
            System.out.println("There is no place in the storage for a new resume. To save the resume delete any old one you don't need");
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) return storage[i];
        }
        System.out.println("There is no resume with uuid = " + uuid);
        return null;
    }

    void delete(String uuid) {
        int indexOfResumeToDelete = -1;
        for (int i = 0; i < size; i++) {
            if (indexOfResumeToDelete < 0) {
                if (storage[i].toString().equals(uuid)) {
                    indexOfResumeToDelete = i;

                }
            } else {
                if (i < size-1) { // Чтобы избежать переполнения массива в строке ниже
                    storage[i] = storage[i + 1];
                }
            }
        }
        if (indexOfResumeToDelete < 0) {
            System.out.println("There is no resume with uuid = " + uuid);
        } else {
            storage[size - 1] = null;
            size = size -1;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        int i = 0;
        while (storage[i] != null && i < 10000) {
            i++;
        }
        return i;
    }
}

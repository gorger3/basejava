/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        for (Resume r : getAll()) {
            r = null;
        }
    }

    void save(Resume r) {
        int size = size();
        if (size < 10000) {
            storage[size] = r;
        } else {
            System.out.println("There is no place in the storage for a new resume. To save the resume delete any old one you don't need");
        }
    }

    Resume get(String uuid) {
        Resume[] all = getAll();
        for (Resume r : all) {
            if (r.toString().equals(uuid)) return r;
        }
        System.out.println("There is no resume with uuid = " + uuid);
        return null;
    }

    void delete(String uuid) {
        Resume[] all = getAll();
        int indexOfResumeToDelete = -1;
        for (int i = 0; i < size(); i++) {
            if (indexOfResumeToDelete < 0) {
                if (all[i].toString().equals(uuid)) {
                    indexOfResumeToDelete = i;

                }
            } else {
                all[i] = storage[i + 1];
            }
        }
        if (indexOfResumeToDelete < 0) {
            System.out.println("There is no resume with uuid = " + uuid);
        } else {
            all[size() - 1] = null;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        int i = 0;
        while (storage[i] != null) {
            i++;
        }
        Resume[] all = new Resume[i];
        for (int j = 0; j < i; j++) {
            all[j] = storage[j];
        }
        return all;
    }

    int size() {
        return getAll().length;
    }
}

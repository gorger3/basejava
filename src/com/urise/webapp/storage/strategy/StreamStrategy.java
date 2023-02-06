package com.urise.webapp.storage.strategy;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SerializationStrategy;

import java.io.*;

public class StreamStrategy implements SerializationStrategy {

    @Override
    public void write(Resume r, OutputStream os) {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        } catch (IOException e) {
            throw new StorageException("Error write resume", r.getUuid(), e);
        }
    }

    @Override
    public Resume read(InputStream is) {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
package com.urise.webapp.storage.strategy;

import com.urise.webapp.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {
    void write(Resume r, OutputStream os);
    Resume read(InputStream is);
}

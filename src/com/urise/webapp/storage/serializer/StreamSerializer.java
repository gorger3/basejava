package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializer {
    void doWrite(Resume r, OutputStream os);
    Resume doRead(InputStream is);
}

package com.urise.webapp.storage.serializer;

import java.io.IOException;

@FunctionalInterface
public interface DataConsumer<T> {
    void writeData(T t) throws IOException;
}

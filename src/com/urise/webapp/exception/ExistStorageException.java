package com.urise.webapp.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(Object o) {
        super("Resume " + o.toString() + " already exist", o.toString());
    }
}

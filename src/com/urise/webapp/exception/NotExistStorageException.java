package com.urise.webapp.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(Object o) {
        super("Resume " + o.toString() + " not exist", o.toString());
    }
}

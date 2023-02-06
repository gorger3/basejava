package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * gkislin
 * 22.07.2016
 */
public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;

    private final SerializationStrategy strategy;

    protected void doWrite(Resume r, OutputStream os) {
        strategy.write(r, os);
    }

    protected Resume doRead(InputStream is) {
        return strategy.read(is);
    }

    protected PathStorage(String dir, SerializationStrategy strategy) {
        directory = Paths.get(dir);
        this.strategy = strategy;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory + " is not directory");
        }
    }

    @Override
    public void clear() {
        getPathStream().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getPathStream().count();
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + path.toAbsolutePath(), path.getFileName().toString(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getPathStream().map(path -> doGet(path)).collect(Collectors.toList());
    }

    private Stream<Path> getPathStream() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Get paths from directory error", null);
        }
    }
}
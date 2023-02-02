package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * gkislin
 * 21.07.2016
 */
public class MainFile {
    public static void main(String[] args) {

        String filePath = ".\\.gitignore";
        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // обход папки src
        String srcPath = "C:\\Users\\GGorelik\\Documents\\java-study\\javaops\\basejava\\src";
        File rootDir = new File(srcPath);
        List<File> listOfFiles = new ArrayList<>();
        System.out.println("----------------- printDirectoryDeeply ------------------------ ");
        System.out.println(printDirectoryDeeply(rootDir, listOfFiles));
        System.out.println("----------------- walkThroughDirectoryWithIndentation ------------------------ ");
        walkThroughDirectoryWithIndentation(rootDir.toPath());
    }

    // метод для глубокого обхода папки: возвращает все файлы в папке
    public static List<File> printDirectoryDeeply(File rootDir, List<File> list) {
        File[] files = rootDir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("File: " + file.getName());;
            } else {
                System.out.println("Directory: " + file.getName());
                printDirectoryDeeply(file, list);
            }
        }
        return list;
    }

    // метод для глубокого обхода папки с использованием NIO
    public static void walkThroughDirectoryWithIndentation(Path rootDir) {
        try {
            Files.walkFileTree(rootDir, new MyFileVisitor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MyFileVisitor extends SimpleFileVisitor {

        @Override
        public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);
            System.out.println("Directory: " + ((Path) dir).getFileName());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Object file, BasicFileAttributes attrs)
                throws IOException {
            Objects.requireNonNull(file);
            Objects.requireNonNull(attrs);
            System.out.println("  " + ((Path) file).getFileName());
            return FileVisitResult.CONTINUE;
        }
    }
}
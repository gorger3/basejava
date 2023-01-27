package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
//        getFilesInDirectory(srcPath, listOfFiles);
        System.out.println("args = " + getFilesInDirectory(rootDir, listOfFiles));

    }
    // метод для обхода папки: возвращает все файлы в папке
    public static List<File> getFilesInDirectory(File rootDir, List<File> list) {
        File[] files = rootDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getFilesInDirectory(file, list);
            } else {
                list.add(file);
            }
        }
        return list;
    }
}
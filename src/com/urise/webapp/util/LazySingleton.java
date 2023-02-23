package com.urise.webapp.util;

public class LazySingleton {
    volatile private static LazySingleton INSTANCE;

    private LazySingleton() {

    }

    private class LazySingletonHolder {
        private final static LazySingleton INSTANCE = new LazySingleton();
    }

        public LazySingleton getInstance() {
        return LazySingletonHolder.INSTANCE;
//        if (INSTANCE == null) {
//            synchronized (LazySingleton.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new LazySingleton();
//                }
//            }
//        }
//        return INSTANCE;
//
    }
}

package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        Resume r = new Resume();
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        System.out.println(r);

        // invoke r.toString via reflection
        Method[] declaredMethods = r.getClass().getDeclaredMethods();
        int count = 0;
        int index = 0;
        for (Method m : declaredMethods) {
            if (m.getName().equals("toString")) {
                index = count;
            }
            count++;
        }
        Method method = r.getClass().getDeclaredMethods()[index];
        method.setAccessible(true);
        System.out.println(method.invoke(r));
    }
}
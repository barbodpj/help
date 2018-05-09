package model;

import model.troop.Troop;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUI {
    public static Object getInstanceFromName(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> wantedClass = Class.forName(name);
        Object object = wantedClass.newInstance();
        return object;
    }

    public static Object getInstanceFromName ( String name  , int level) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> wantedClass = Class.forName(name);
        Object object =  wantedClass.getClass().getDeclaredConstructor(int.class).newInstance();
        return object;
    }
}

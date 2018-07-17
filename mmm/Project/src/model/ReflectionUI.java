package model;

import model.building.Building;
import model.troop.Troop;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUI {
    public static Object getInstanceFromName(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> wantedClass = Class.forName(name);
        Object object = wantedClass.getDeclaredConstructor(int.class , model.Village.class).newInstance( -1 , null);
        return object;
    }

    public static Object getInstanceFromName ( String name  , int level , Village village ) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> wantedClass = Class.forName(name);
        Object object =  wantedClass.getDeclaredConstructor(int.class , Village.class ).newInstance(level , village );
        return object;
    }

    public static Object getInstanceFromNameAndLocation ( String name , Location location) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> wantedClass = Class.forName(name);

        Object object =  wantedClass.getDeclaredConstructor(model.Location.class , Village.class).newInstance( location , null);

        return object;


    }

    public static Object getInstanceFromNameAndLocation ( String name , Location location , int amount) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> wantedClass = Class.forName(name);

        Object object =  wantedClass.getDeclaredConstructor(Location.class , Village.class , int.class).newInstance(location , null , amount);

        return object;


    }

}


package net.sehales.secon.utils.java;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
    
    public static Object getDeclaredFieldValue(Class<?> clazz, Object fromObject, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field f = clazz.getDeclaredField(fieldName);
        boolean accessible = f.isAccessible();
        f.setAccessible(true);
        Object value = f.get(fromObject);
        f.setAccessible(accessible);
        return value;
    }
    
    public static Object getDeclaredFieldValue(Object object, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        return getDeclaredFieldValue(object.getClass(), object, fieldName);
    }
    
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(methodName, parameterTypes);
    }
    
    public static Object invokeMethod(Method method, Object caller, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object result = method.invoke(caller, args);
        method.setAccessible(accessible);
        return result;
    }
    
}

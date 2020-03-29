package org.myspring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class ReflectionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);


    public static Object newInstance(Class<?> cls) {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOG.error("new instance failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(Object obj, Method method, Object... parameter) {
        method.setAccessible(true);
        try {
            return method.invoke(obj, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOG.error("invoke method failure", e);
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            LOG.error("set field failure.", e);
            throw new RuntimeException(e);
        }
    }

}

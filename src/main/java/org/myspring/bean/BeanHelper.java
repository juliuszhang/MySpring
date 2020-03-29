package org.myspring.bean;

import org.myspring.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class BeanHelper {

    private static final Logger LOG = LoggerFactory.getLogger(BeanHelper.class);

    private static final Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet) {
            Object bean = ReflectionUtil.newInstance(beanClass);
            BEAN_CONTAINER.put(beanClass, bean);
        }
    }


    public static <T> T getBean(Class<T> cls) {
        Object bean = BEAN_CONTAINER.get(cls);
        if (bean == null) {
            LOG.error("can not get bean by class: {}" + cls.toString());
            throw new RuntimeException("can not get bean by class:" + cls);
        }
        return (T) BEAN_CONTAINER.get(cls);
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return Collections.unmodifiableMap(BEAN_CONTAINER);
    }
}

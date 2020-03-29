package org.myspring.bean;

import org.myspring.annotation.Autowired;
import org.myspring.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class IocHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
            Class<?> beanClass = beanEntry.getKey();
            Object bean = beanEntry.getValue();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldType = field.getType();
                    Object injectBean = beanMap.get(fieldType);
                    if (injectBean != null) {
                        ReflectionUtil.setField(bean, field, injectBean);
                    }
                }
            }
        }
    }

}

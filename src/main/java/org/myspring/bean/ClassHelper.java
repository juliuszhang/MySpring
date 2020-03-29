package org.myspring.bean;

import org.myspring.annotation.Controller;
import org.myspring.annotation.Service;
import org.myspring.config.ConfigHelper;
import org.myspring.util.ClassUtil;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public final class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getBasePackage();
        CLASS_SET = ClassUtil.loadClasses(basePackage);
    }

    public static Set<Class<?>> getBeanClassSet() {
        return Collections.unmodifiableSet(CLASS_SET);
    }

    public static Set<Class<?>> getServiceClassSet() {
        return CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(Service.class)).collect(Collectors.toSet());
    }

    public static Set<Class<?>> getControllerClassSet() {
        return CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(Controller.class)).collect(Collectors.toSet());
    }

}

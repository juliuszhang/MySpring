package org.myspring;

import org.myspring.aspect.AopHelper;
import org.myspring.bean.BeanHelper;
import org.myspring.bean.ClassHelper;
import org.myspring.bean.IocHelper;
import org.myspring.mvc.ControllerHelper;
import org.myspring.util.ClassUtil;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), false);
        }

    }

}

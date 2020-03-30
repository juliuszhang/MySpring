package org.myspring.annotation;

import java.lang.annotation.*;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();

}

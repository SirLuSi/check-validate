package com.lusi.framework.annotation;

import java.lang.annotation.*;

/**
 * @author lusi
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CheckValid {
    Class<?>[] value() default {};
}

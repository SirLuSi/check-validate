/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lusi.framework.config;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Simple MethodMatcher that looks for a specific Java 5 annotation
 * being present on a method (checking both the method on the invoked
 * interface, if any, and the corresponding method on the target class).
 *
 * @author Juergen Hoeller
 * @see AnnotationMatchingPointcut
 * @since 2.0
 */
public class AnnotationParamMatcher extends StaticMethodMatcher {

    private final Class<? extends Annotation> annotationType;

    private final boolean checkInherited;


    /**
     * Create a new AnnotationClassFilter for the given annotation type.
     *
     * @param annotationType the annotation type to look for
     */
    public AnnotationParamMatcher(Class<? extends Annotation> annotationType) {
        this(annotationType, false);
    }

    /**
     * Create a new AnnotationClassFilter for the given annotation type.
     *
     * @param annotationType the annotation type to look for
     * @param checkInherited whether to also check the superclasses and
     *                       interfaces as well as meta-annotations for the annotation type
     *                       (i.e. whether to use {@link AnnotatedElementUtils#hasAnnotation}
     *                       semantics instead of standard Java {@link Method#isAnnotationPresent})
     * @since 5.0
     */
    public AnnotationParamMatcher(Class<? extends Annotation> annotationType, boolean checkInherited) {
        Assert.notNull(annotationType, "Annotation type must not be null");
        this.annotationType = annotationType;
        this.checkInherited = checkInherited;
    }


    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (matchesParam(method)) {
            return true;
        }
        // Proxy classes never have annotations on their redeclared methods.
        if (Proxy.isProxyClass(targetClass)) {
            return false;
        }
        // The method may be on an interface, so let's check on the target class as well.
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        return (specificMethod != method && matchesParam(specificMethod));
    }

    private boolean matchesParam(Method method) {
        //checkInherited  为true时会查找方法上的注解是否包含目标注解 为false只会判断该方法是否包含此注解
        //        return (this.checkInherited ? AnnotatedElementUtils.hasAnnotation(method, this.annotationType) :
        //                method.isAnnotationPresent(this.annotationType));
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType().isAssignableFrom(annotationType)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AnnotationParamMatcher)) {
            return false;
        }
        AnnotationParamMatcher otherMm = (AnnotationParamMatcher) other;
        return this.annotationType.equals(otherMm.annotationType);
    }

    @Override
    public int hashCode() {
        return this.annotationType.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + this.annotationType;
    }

}

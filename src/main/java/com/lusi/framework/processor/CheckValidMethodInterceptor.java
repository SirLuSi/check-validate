package com.lusi.framework.processor;

import com.lusi.framework.annotation.CheckValid;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author lusi
 */
public class CheckValidMethodInterceptor implements MethodInterceptor, Ordered {

    private Validator validator;

    private AbstractReturnDefinition abstractReturnDefinition;

    public CheckValidMethodInterceptor(Validator validator, AbstractReturnDefinition abstractReturnDefinition) {
        this.validator = validator;
        this.abstractReturnDefinition = abstractReturnDefinition;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (this.isFactoryBeanMetadataMethod(invocation.getMethod())) {
            return invocation.proceed();
        } else {
            Method methodToValidate = invocation.getMethod();

            Set result = new HashSet();
            try {
                result = validate(invocation);
                if (result.isEmpty()) {
                    result = validator.forExecutables().validateParameters(invocation.getThis(), methodToValidate, invocation.getArguments());
                }
            } catch (IllegalArgumentException var7) {
//                methodToValidate = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
//                result = execVal.validateParameters(invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
            }
            //验证全部通过
            if (result.isEmpty()) {
                return invocation.proceed();
            }

            if (Objects.isNull(abstractReturnDefinition)) {
                throw new ConstraintViolationException(result);
            } else {
                return abstractReturnDefinition._return(result, invocation.getMethod().getReturnType());
            }


        }
    }

    Set validate(MethodInvocation invocation) {

        Annotation[][] parameterAnnotations = invocation.getMethod().getParameterAnnotations();
        Object[] parameterTypes = invocation.getArguments();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if (parameterAnnotations[i][j] instanceof CheckValid) {
                    return validator.validate(parameterTypes[i], ((CheckValid) parameterAnnotations[i][j]).value());
                }

            }
        }
        return Collections.emptySet();
    }

    private boolean isFactoryBeanMetadataMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        if (clazz.isInterface()) {
            return (clazz == FactoryBean.class || clazz == SmartFactoryBean.class) && !method.getName().equals("getObject");
        } else {
            Class<?> factoryBeanType = null;
            if (SmartFactoryBean.class.isAssignableFrom(clazz)) {
                factoryBeanType = SmartFactoryBean.class;
            } else if (FactoryBean.class.isAssignableFrom(clazz)) {
                factoryBeanType = FactoryBean.class;
            }

            return factoryBeanType != null && !method.getName().equals("getObject") && ClassUtils.hasMethod(factoryBeanType, method.getName(), method.getParameterTypes());
        }
    }

    protected Class<?>[] determineValidationGroups(@CheckValid MethodInvocation invocation) {
        CheckValid validatedAnn = AnnotationUtils.findAnnotation(invocation.getMethod(), CheckValid.class);


        return validatedAnn != null ? validatedAnn.value() : new Class[0];
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}

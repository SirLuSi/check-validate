package com.lusi.framework.processor;

import com.lusi.framework.annotation.CheckValid;
import com.lusi.framework.config.AnnotationMatchingPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.lang.Nullable;

import javax.validation.Validator;
import java.lang.annotation.Annotation;

/**
 * @author lusi
 */
public class CheckValidAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public CheckValidAdvisor(@Nullable Validator validator, AbstractReturnDefinition returnDefinition) {
        this.advice = buildAdvice(validator, returnDefinition);
        this.pointcut = buildPointcut(CheckValid.class);
    }

    protected Advice buildAdvice(@Nullable Validator validator, AbstractReturnDefinition returnDefinition) {

        return new CheckValidMethodInterceptor(validator, returnDefinition);
    }


    protected Pointcut buildPointcut(Class<? extends Annotation> annotationType) {
        Pointcut cpc = new AnnotationMatchingPointcut(annotationType, true);
        Pointcut mpc = new AnnotationMatchingPointcut(null, annotationType, true);
        Pointcut ppc = new AnnotationMatchingPointcut(annotationType);
        ComposablePointcut result = new ComposablePointcut(cpc);
        result.union(cpc);
        result.union(mpc);
        result.union(ppc);
        return result;
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }


}

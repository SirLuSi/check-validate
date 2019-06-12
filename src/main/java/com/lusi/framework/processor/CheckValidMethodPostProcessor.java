package com.lusi.framework.processor;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;

import javax.validation.Validator;

/**
 * @author lusi
 */
public class CheckValidMethodPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {


    @Nullable
    private Validator validator;

    private AbstractReturnDefinition returnDefinition;

    public CheckValidMethodPostProcessor() {
        setBeforeExistingAdvisors(true);
    }


    public void setValidator(Validator validator) {
        this.validator = validator;
    }


    public void setReturnDefinition(AbstractReturnDefinition returnDefinition) {
        this.returnDefinition = returnDefinition;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.advisor = new CheckValidAdvisor(validator, returnDefinition);
    }


}

package com.lusi.framework.config;

import com.lusi.framework.annotation.EnableValidate;
import com.lusi.framework.processor.AbstractReturnDefinition;
import com.lusi.framework.processor.CheckValidMethodPostProcessor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author lusi
 */
@Configuration
public class ValidateAutoConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes enableAsync;

    @Bean("hibernateValidator")
    @Role(2)
    @ConditionalOnMissingBean({Validator.class})
    public static LocalValidatorFactoryBean hibernateValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        factoryBean.setProviderClass(HibernateValidator.class);
        return factoryBean;
    }


    @Bean("checkValidMethodPostProcessor")
    public CheckValidMethodPostProcessor methodValidationPostProcessor(@Qualifier("hibernateValidator") LocalValidatorFactoryBean factoryBean,
                                                                       @Autowired(required = false) AbstractReturnDefinition returnDefinition) {
        CheckValidMethodPostProcessor processor = new CheckValidMethodPostProcessor();
        processor.setProxyTargetClass(this.enableAsync.getBoolean("proxyTargetClass"));
        processor.setValidator(factoryBean);
        processor.setReturnDefinition(returnDefinition);
        processor.setOrder(this.enableAsync.<Integer>getNumber("order"));
        return processor;
    }


    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableAsync = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableValidate.class.getName(), false));
    }
}

package com.lusi.framework.processor;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author lusi
 */
public abstract class AbstractReturnDefinition {

    public abstract Object _return(Set<? extends ConstraintViolation<?>> constraintViolations, Class returnType);

}

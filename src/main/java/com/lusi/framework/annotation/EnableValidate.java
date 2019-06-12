package com.lusi.framework.annotation;

import com.lusi.framework.config.ValidateAutoSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author lusi
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({ValidateAutoSelector.class})
public @interface EnableValidate {

    /**
     * Indicate whether subclass-based (CGLIB) proxies are to be created ({@code true}) as
     * opposed to standard Java interface-based proxies ({@code false}). The default is
     * {@code false}. <strong>Applicable only if {@link #mode()} is set to
     * {@link AdviceMode#PROXY}</strong>.
     * <p>Note that setting this attribute to {@code true} will affect <em>all</em>
     * Spring-managed beans requiring proxying, not just those marked with
     * {@code @Transactional}. For example, other beans marked with Spring's
     * {@code @Async} annotation will be upgraded to subclass proxying at the same
     * time. This approach has no negative impact in practice unless one is explicitly
     * expecting one type of proxy vs another, e.g. in tests.
     */
    boolean proxyTargetClass() default false;


    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * Indicate the ordering of the execution of the transaction advisor
     * when multiple advices are applied at a specific joinpoint.
     * <p>The default is {@link Ordered#LOWEST_PRECEDENCE}.
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}

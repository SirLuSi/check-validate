package com.lusi.framework.config;

import com.lusi.framework.annotation.EnableValidate;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

public class ValidateAutoSelector extends AdviceModeImportSelector<EnableValidate> {


    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{ValidateAutoConfiguration.class.getName()};
    }
}

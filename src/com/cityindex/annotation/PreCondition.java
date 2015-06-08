package com.cityindex.annotation;

import com.cityindex.param.ConfigParam;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreCondition {
    Condition[] preConditions() default {Condition.NONE};

    boolean checkInternetState() default true;
    // prepareLogin
    ConfigParam login() default ConfigParam.LOGIN;
    ConfigParam password() default ConfigParam.PASSWORD;

    // TestCase
    String testTitle();
    long testId();


}

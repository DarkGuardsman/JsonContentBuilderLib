package com.builtbroken.builder.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation added to methods and fields to indicate how to inject JSON
 * data.
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD})
public @interface JsonMapping
{

    /**
     * List of keys to use for matching json
     */
    String[] keys();

    /**
     * Data type to load data as, optional for boolean and strings
     */
    String type() default "*"; //* acts as a indicator to attempt to match field

    /**
     * Arguments to pass into type converter, optional in most cases
     */
    String[] args() default "";

    /**
     * Enforced that a value is not null and contains data
     * <p>
     * Only works on fields at this time.
     *
     * @return true to enforce a not null state
     */
    boolean required() default false;

}

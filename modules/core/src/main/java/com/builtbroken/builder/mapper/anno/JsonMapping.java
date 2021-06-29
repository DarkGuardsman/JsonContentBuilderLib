package com.builtbroken.builder.mapper.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to understand how to map json information to
 * fields, methods, or parameters in constructors.
 * <p>
 * When used on a field it will set the field with the generated object
 * <p>
 * When used on a method it will invoke the method with the generated object
 * <p>
 * When used on a parameter in a constructor it will map that parameter
 * to json data requested. Not yet supported for methods.
 * <p>
 * Created by Robin Seifert on 2019-03-11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface JsonMapping
{

    /**
     * List of keys to use for matching json
     */
    String[] keys();

    /**
     * Data type to use for conversion process
     */
    String type();

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

    /**
     * Marks the mapping as a legacy conversion for older files
     *
     * @return true to note legacy and to generate warnings
     */
    boolean legacy() default false;

}

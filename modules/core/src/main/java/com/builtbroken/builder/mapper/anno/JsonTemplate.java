package com.builtbroken.builder.mapper.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a class for automatic registry as a template.
 * <p>
 * This will generate a builder for creating the object and
 * search the class for any annotations part of the system.
 * <p>
 * A default constructor will be required if {@link JsonConstructor}
 * is not present on any constructor or static method.
 * <p>
 * It is not recommend to use this with parented objects to
 * avoid any problems or expectations of functionality by
 * the generated object.
 * <p>
 * Created by Robin Seifert on 2019-05-14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface JsonTemplate
{

    /**
     * JsonObjectHandler to map the objects using this template to registry matching.
     * <p>
     * By default every template gets a unique registry but this is not always desired. Especially
     * in cases were several templates output the same object sets.
     */
    String registry() default "";

    /** Unique content loader ID */
    String value();

    /**
     * Content loader to register for
     *
     * @return unique id of loader
     */
    String contentLoader() default "";

    /**
     * Should a default constructor be used to create this object
     * in place of using {@link JsonConstructor}
     *
     * @return true to use default constructor
     */
    boolean useDefaultConstructor() default false;
}

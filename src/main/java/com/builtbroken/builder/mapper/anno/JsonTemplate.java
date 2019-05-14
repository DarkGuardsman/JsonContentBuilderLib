package com.builtbroken.builder.mapper.anno;

import com.builtbroken.builder.ContentBuilderRefs;

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
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface JsonTemplate
{

    /**
     * Type of json object this template represents
     * Ex: armory:sword
     */
    String type();

    /**
     * Content loader to register for
     *
     * @return unique id of loader
     */
    String contentLoader() default ContentBuilderRefs.MAIN_LOADER;

    /**
     * Should a default constructor be used to create this object
     * in place of using {@link JsonConstructor}
     *
     * @return true to use default constructor
     */
    boolean useDefaultConstructor() default false;
}

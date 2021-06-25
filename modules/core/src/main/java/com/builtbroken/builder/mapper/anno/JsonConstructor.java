package com.builtbroken.builder.mapper.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a constructor or method in a class to build a json object.
 * <p>
 * In order to use this on a method it needs to be static.
 * <p>
 * By default the method and constructor version will support
 * passing in the original JSON object. This can be used to load
 * a few properties though is not recommended. Instead use the
 * annotations to map properties directly.
 * <p>
 * Do not change the passed in data as this may not be
 * supported in future releases.
 * <p>
 * Created by Robin Seifert on 2019-05-14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
public @interface JsonConstructor
{

    /**
     * Json Object type to build as, use this to allow a java object to support building different json objects
     */
    String type() default "*";

    /**
     * Set to true to use the constructor object from the JSON data.
     * By default the Json data is designed with a type, and data set for reading information.
     * Normally it would read from the data set but if the data needs to be separate for some reason
     * then this can be used as a quick switch. Anything more complex should use the pipeline to
     * define a custom read of the JSON file.
     *
     * @return
     */
    boolean useConstructorData() default false;
}

package com.builtbroken.builder.mapper.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to flag a method for json injection
 * <p>
 * Paramaters of the method require {@link JsonMapping} for this
 * to work fully
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface JsonInjection
{

}

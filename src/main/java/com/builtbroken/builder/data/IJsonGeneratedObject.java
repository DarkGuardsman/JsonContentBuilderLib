package com.builtbroken.builder.data;

/**
 * Applied to objects that will be generated from a
 * JSON template and registered to a handler for use in other systems.
 * <p>
 * Created by Robin Seifert on 2019-03-11.
 */
public interface IJsonGeneratedObject
{

    /**
     * Type of JSON template this object was generated using
     */
    String getJsonType();

    /**
     * Unique ID withing the {@link #getJsonType()}
     */
    String getJsonUniqueID();
}

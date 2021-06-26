package com.builtbroken.builder.data;

/**
 * Applied to objects that were generated from a
 * JSON template and registered to a handler for use in other systems.
 * <p>
 * Created by Robin Seifert on 2019-03-11.
 */
public interface IJsonGeneratedObject
{

    /**
     * Type of JSON registry used for this object
     */
    default String getJsonRegistryID() {
        return getJsonTemplateID();
    }

    /**
     * Type of JSON template this object was generated using
     */
    String getJsonTemplateID();

    /**
     * Unique ID of this object for the given {@link #getJsonRegistryID()}
     */
    String getJsonUniqueID();
}

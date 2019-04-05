package com.builtbroken.builder.mapper.linker;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public interface IJsonLinker<O extends Object>
{

    /**
     * Called to map json data to the location
     *
     * @param object
     * @param data
     * @param registry
     */
    void link(O object, JsonElement data, JsonObjectHandlerRegistry registry);

    /**
     * Checks if the mapper has
     * valid data for the field or
     * method it set.
     *
     * @return
     */
    boolean isValid(O object);

    /**
     * Handler to match on
     *
     * @return
     */
    String getType();

    /**
     * Json field to match on
     *
     * @return
     */
    String[] getKeys();

    /**
     * Called to destroy the linker and release references
     */
    void destroy();
}

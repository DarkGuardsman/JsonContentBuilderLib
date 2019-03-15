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
     * @param converter
     */
    void map(O object, JsonElement data, JsonObjectHandlerRegistry registry);

    /**
     * Checks if the mapper has
     * valid data for the field or
     * method it set.
     *
     * @return
     */
    boolean isValid(O object);

    /**
     * Array of keys that JSON maps to this link
     *
     * @return
     */
    String[] getKeys();
}

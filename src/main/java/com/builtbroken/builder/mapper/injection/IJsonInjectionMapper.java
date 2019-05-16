package com.builtbroken.builder.mapper.injection;

import com.builtbroken.builder.converter.ConversionHandler;
import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-16.
 */
public interface IJsonInjectionMapper<O extends Object>
{
    /**
     * Called to map json data to the location
     *
     * @param object
     * @param data
     * @param converter
     */
    void map(O object, JsonElement data, ConversionHandler converter);
}

package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.data.IJsonUnique;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Robin Seifert on 2019-05-14.
 */
public interface IJsonBuilder<O> extends IJsonUnique
{

    /**
     * Creates a new object using the provided data
     *
     * @param data      - data to create the object, may optionally be used
     * @param converter - handler used to map json element data
     * @return created object
     */
    O newObject(@Nonnull JsonElement data, @Nonnull ConversionHandler converter)
            throws IllegalAccessException, InvocationTargetException, InstantiationException;
}

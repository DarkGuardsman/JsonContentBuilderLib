package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class JsonBuilderConstructor extends JsonBuilderMapper
{
    protected final Constructor constructor;

    public JsonBuilderConstructor(Class clazz, String type, Constructor constructor, JsonMapping[] mappers, boolean useConstructorData)
    {
        super(clazz, type, mappers, useConstructorData);
        this.constructor = constructor;
    }

    @Override
    public Object newObject(@Nonnull JsonElement data, @Nonnull ConversionHandler converter) throws IllegalAccessException, InvocationTargetException, InstantiationException
    {
        if (mappers != null)
        {
            //can only map with a JSON object as we need to get the data
            if (data.isJsonObject())
            {
                final Object[] args = buildParameters(converter, data.getAsJsonObject());

                //Create
                return constructor.newInstance(args);
            }
            else
            {
                throw new RuntimeException("JsonBuilderConstructor: can not create object with parameters due to JSON not being an object.");
            }
        }
        //Json element input, not need to check type as it should be valid or its broken thus nothing will work anyways
        else if (constructor.getParameterCount() == 1)
        {
            return constructor.newInstance(data);
        }

        //Default, honestly shouldn't be used
        return constructor.newInstance();
    }
}

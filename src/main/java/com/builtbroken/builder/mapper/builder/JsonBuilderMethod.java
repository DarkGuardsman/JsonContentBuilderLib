package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class JsonBuilderMethod extends JsonBuilderMapper
{
    protected final Method method;

    public JsonBuilderMethod(Class clazz, String type, Method method, BiFunction<JsonObject, ConversionHandler, Object>[] mappers, boolean useConstructorData)
    {
        super(clazz, type, mappers, useConstructorData);
        this.method = method;
    }

    @Override
    public Object newObject(@Nonnull JsonElement data, @Nonnull ConversionHandler converter) throws InvocationTargetException, IllegalAccessException
    {
        if (mappers != null)
        {
            //can only map with a JSON object as we need to get the data
            if (data.isJsonObject())
            {
                final Object[] args = buildParameters(converter, data.getAsJsonObject());

                //Create
                return method.invoke(null, args);
            }
            else
            {
                throw new RuntimeException("JsonBuilderMethod: can not create object with parameters due to JSON not being an object.");
            }
        }
        //Json element input, not need to check type as it should be valid or its broken thus nothing will work anyways
        else if (method.getParameterCount() == 1)
        {
            return method.invoke(null, data);
        }

        //Default, honestly shouldn't be used
        return method.invoke(null);
    }
}

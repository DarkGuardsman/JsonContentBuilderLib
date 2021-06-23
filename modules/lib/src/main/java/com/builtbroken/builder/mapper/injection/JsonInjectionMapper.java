package com.builtbroken.builder.mapper.injection;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.mapper.MapperHelpers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiFunction;

import static com.builtbroken.builder.mapper.mappers.JsonMapper.getEnumValue;

/**
 * Created by Robin Seifert on 2019-03-11.
 */
public class JsonInjectionMapper implements IJsonInjectionMapper
{

    private final Method method;
    private final BiFunction<JsonObject, ConversionHandler, Object>[] mappers;

    public JsonInjectionMapper(Method method, BiFunction<JsonObject, ConversionHandler, Object>[] mappers)
    {
        this.method = method;
        this.mappers = mappers;
    }

    @Override
    public void map(Object object, JsonElement data, ConversionHandler converter)
    {
        try
        {
            if (mappers != null)
            {
                //can only map with a JSON object as we need to get the data
                if (data.isJsonObject())
                {
                    final Object[] args = MapperHelpers.buildInputs(data.getAsJsonObject(), converter, mappers);

                    //Invoke
                    method.invoke(object, args);
                }
                else
                {
                    throw new RuntimeException("JsonInjectionMapper: can not inject with parameters due to JSON not being an object.");
                }
            }
        } catch (Exception e)
        {
            throw new RuntimeException("JsonInjectionMapper: Failed to inject json objects into " +
                    "method " + method.getName()
                    + "\n OBJ: " + object
                    + "\n JSON: " + data,
                    e);
        }
    }

    @Override
    public String toString()
    {
        return "JsonInjectionMapper[" + method + "]";
    }
}

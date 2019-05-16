package com.builtbroken.builder.mapper;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.mappers.JsonMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-16.
 */
public class MapperHelpers
{

    /**
     * Called to generate mappers for a method or constructor
     *
     * @param types     - clazz types of inputs
     * @param paraAnnos - annotations on inputs
     * @param errorData - error information to append to any throws
     * @return array of functions by inputs
     */
    public static BiFunction<JsonObject, ConversionHandler, Object>[] buildMappers(final Class[] types, final Annotation[][] paraAnnos, final Supplier<String> errorData)
    {
        final BiFunction<JsonObject, ConversionHandler, Object>[] mappers = new BiFunction[types.length];
        for (int para = 0; para < mappers.length; para++)
        {
            final int paraC = para;
            final Class<?> paraClazz = types[para];
            for (Annotation annotation : paraAnnos[para])
            {
                if (annotation instanceof JsonMapping)
                {
                    if (annotation instanceof JsonMapping)
                    {
                        mappers[para] = MapperHelpers.getClazzBasedMapper(paraClazz, (JsonMapping) annotation, () -> errorData.get() + " PARA: " + paraC);
                        break;
                    }
                    break;
                }
            }

            if (mappers[para] == null)
            {
                new RuntimeException("JsonClassMapper: All parameters require JsonMapping annotation"
                        + " when used to create mapping logic for injection into a method or constructor."
                        + errorData.get());
            }
        }
        return mappers;
    }

    /**
     * Called to generate the inputs from the JSON input using the provided converter and mappers
     *
     * @param jsonObject - data
     * @param converter  - handler to do the conversions
     * @param mappers    - functions to provide logic for how to convert the input data
     * @return array of inputs for injection or mapping
     */
    public static Object[] buildInputs(JsonObject jsonObject, ConversionHandler converter, BiFunction<JsonObject, ConversionHandler, Object>[] mappers)
    {
        //Map args
        final Object[] args = new Object[mappers.length];
        for (int i = 0; i < mappers.length; i++)
        {
            final BiFunction<JsonObject, ConversionHandler, Object> mapper = mappers[i];
            final Object out = mapper.apply(jsonObject, converter);
            if (out != null)
            {
                args[i] = out;
            }
        }

        return args;
    }

    /**
     * Gets a BiFunction that can be used to map an input from JSON based on the JSON and clazz of the target
     *
     * @param paraClazz - target field or parameter's class
     * @param mapper    - annotation that was on the mapping
     * @param error     - supplier for error information in case something fails later
     * @return function
     */
    public static BiFunction<JsonObject, ConversionHandler, Object> getClazzBasedMapper(Class<?> paraClazz, JsonMapping mapper, Supplier<String> error)
    {
        return (jsonObject, converter) ->
        {
            final String type = mapper.type();
            Object object = null;
            for (final String key : mapper.keys())
            {
                final JsonElement json = jsonObject.get(key);

                if (json != null)
                {
                    //Special handling for enum
                    if (type.equalsIgnoreCase(ConverterRefs.ENUM))
                    {
                        try
                        {
                            object = JsonMapper.getEnumValue(paraClazz, json);
                        } catch (Exception e)
                        {
                            throw new RuntimeException("JsonBuilderMapper: Failed to get enum due", e);
                        }
                    }
                    //Normal handling
                    else
                    {
                        object = converter.fromJson(type, json, mapper.args());
                    }
                }

                if (json != null)
                {
                    break;
                }
            }

            if (object == null && mapper.required())
            {
                throw new RuntimeException("JsonBuilderMapper: Failed to load required json field while mapping for a builder. " + error.get());
            }
            return object;
        };
    }
}

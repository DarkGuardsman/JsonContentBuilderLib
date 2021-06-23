package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Created by Robin Seifert on 2019-03-11.
 */
public class JsonMethodMapper extends JsonMapper<Object>
{

    private final Class clazz;
    private final Method method;

    public JsonMethodMapper(Class clazz, Method method, JsonMapping mapping)
    {
        this(clazz, method, mapping.keys(), mapping.type(), mapping.args());
    }

    public JsonMethodMapper(Class clazz, Method method, String[] names, String type, String[] args)
    {
        super(names, type, args);
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public void map(Object object, JsonElement data, ConversionHandler converter)
    {
        try
        {
            method.setAccessible(true);

            //Special case for enums
            if (type.equalsIgnoreCase(ConverterRefs.ENUM))
            {
                method.invoke(object, getEnumValue(method.getParameterTypes()[0], data));
            }
            //Normal handling
            else
            {
                final Object generatedObject = converter.fromJson(type, data, args);
                //TODO make throw errors if fails to generate instead of NPE check
                if (generatedObject != null)
                {
                    method.invoke(object, generatedObject);
                }
                else
                {
                    throw new RuntimeException("Failed to generate object");
                }
            }
        } catch (Exception e)
        {
            throw new RuntimeException("JsonMethodMapper: Failed to map json data to " +
                    "method " + method.getName()
                    + "\n TYPE: " + type
                    + "\n ARGS: " + Arrays.toString(args)
                    + "\n OBJ: " + object
                    + "\n JSON: " + data,
                    e);
        }

    }

    @Override
    public String toString()
    {
        return "JsonMethodMapper[Keys: " + Arrays.toString(getKeys()) + "]";
    }
}

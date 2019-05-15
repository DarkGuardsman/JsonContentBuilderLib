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
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
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
            if(type.equalsIgnoreCase(ConverterRefs.ENUM))
            {
                handleEnum(object, data);
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
        }
        catch (Exception e)
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

    private void handleEnum(Object objectToSetFieldOn, JsonElement data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final Class type = method.getParameterTypes()[0];
        if(type.isEnum()) //TODO convert to special case injection
        {
            if(!data.isJsonPrimitive())
            {
                throw new RuntimeException("JsonFieldMapper: Fields using 'ENUM' conversion type require"
                        + " json to be a primitive such as String or Integer"
                        + " CLASS: " + clazz
                        + " METHOD: " + method
                        + " JSON: " + data);
            }


            final Class<? extends Enum> enumClass = type.asSubclass(Enum.class);

            final Method valuesMethod = enumClass.getDeclaredMethod("values");
            final Enum[] ens = (Enum[])valuesMethod.invoke(null);

            if(data.getAsJsonPrimitive().isString())
            {
                final String value = data.getAsString();
                for (Enum en : ens)
                {
                    if (en.name().equalsIgnoreCase(value))
                    {
                        method.invoke(objectToSetFieldOn, en);
                        break;
                    }
                }
            }
            else if(data.getAsJsonPrimitive().isNumber())
            {
                Enum value = ens[data.getAsJsonPrimitive().getAsInt()];
                //Not even going to check bounds, its broken either way if not in bounds
                method.invoke(objectToSetFieldOn, value);
            }
            else
            {
                throw new RuntimeException("JsonFieldMapper: Fields using 'ENUM' conversion type require"
                        + " json to be a String or Integer"
                        + " CLASS: " + clazz
                        + " METHOD: " + valuesMethod
                        + " JSON: " + data);
            }
        }
    }
}

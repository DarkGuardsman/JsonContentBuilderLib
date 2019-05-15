package com.builtbroken.builder.mapper.mappers;

import com.google.gson.JsonElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public abstract class JsonMapper<O extends Object> implements IJsonMapper<O>
{

    private final String[] names;
    protected final String type;
    protected final String[] args;

    public JsonMapper(String[] names, String type, String[] args)
    {
        this.names = names;
        this.type = type;
        this.args = args;
    }

    @Override
    public boolean isValid(O object)
    {
        return true;
    }

    @Override
    public String[] getKeys()
    {
        return names;
    }

    @Override
    public void destroy()
    {

    }

    /**
     * Gets the enum value from the class using json element as input
     *
     * @param type - enum class, taken from field or paramater
     * @param data - json data, needs to be a string or integer
     * @return enum or null if not found
     * @throws NoSuchMethodException - shouldn't happen unless java changes how enum works
     * @throws InvocationTargetException - shouldn't happen unless java changes how enum works
     * @throws IllegalAccessException
     */
    public static Enum getEnumValue(Class type, JsonElement data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        if (type.isEnum())
        {
            if (!data.isJsonPrimitive())
            {
                throw new IllegalArgumentException("JsonMapper#handleEnum - JSON data needs to be a primitive of type String or Integer");
            }

            final Class<? extends Enum> enumClass = type.asSubclass(Enum.class);

            final Method method = enumClass.getDeclaredMethod("values");
            final Enum[] ens = (Enum[]) method.invoke(null);

            if (data.getAsJsonPrimitive().isString())
            {
                final String value = data.getAsString();
                for (Enum en : ens)
                {
                    if (en.name().equalsIgnoreCase(value))
                    {
                        return en;
                    }
                }
            }
            else if (data.getAsJsonPrimitive().isNumber())
            {
                //Not even going to check bounds, its broken either way if not in bounds
                return ens[data.getAsJsonPrimitive().getAsInt()];
            }
            else
            {
                throw new IllegalArgumentException("JsonMapper#handleEnum - JSON data needs to be a primitive of type String or Integer");
            }
        }
        return null;
    }
}

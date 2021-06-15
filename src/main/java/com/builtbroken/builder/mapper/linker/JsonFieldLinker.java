package com.builtbroken.builder.mapper.linker;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by Robin Seifert on 2019-03-11.
 */
public class JsonFieldLinker extends JsonLinker<Object>
{

    private final Field field;

    public JsonFieldLinker(Field field, JsonObjectWiring mapping)
    {
        this(field, mapping.jsonFields(), mapping.objectType(), mapping.valuePrefix(), mapping.required());
    }

    public JsonFieldLinker(Field field, String[] names, String type, String prefix, boolean required)
    {
        super(names, type, prefix, required);
        this.field = field;
    }

    @Override
    public void link(Object object, JsonElement data, JsonObjectHandlerRegistry registry)
    {
        try
        {
            field.setAccessible(true);

            if (data.isJsonPrimitive() && data.getAsJsonPrimitive().isString())
            {
                final String key = prefix + data.getAsString();
                final IJsonObjectHandler handler = registry.getHandler(getType());
                if (handler != null)
                {
                    final IJsonGeneratedObject objectToLink = handler.getObject(key);
                    if (objectToLink != null)
                    {
                        System.out.println("JsonFieldLinker: linked " + objectToLink);
                        field.set(object, objectToLink);
                    }
                    else
                    {
                        //TODO display warning? Might just leave this to validation
                        System.out.println("JsonFieldLinker: failed to locate object with name[" + key + "] from " + handler);
                    }
                }
                else
                {
                    //TODO display warning? Might just leave this to validation
                    System.out.println("JsonFieldLinker: failed to locate handler for type " + getType());
                }
            }
            else
            {
                throw new RuntimeException("JsonFieldLinker currently only supports using a string as a link key");
            }

        } catch (Exception e)
        {
            throw new RuntimeException("JsonMethodMapper: Failed to link json object to field. "
                    + "\n FIELD:    " + field.getName()
                    + "\n TYPE:     " + type
                    + "\n OBJ:      " + object
                    + "\n JSON:     " + data,
                    e);
        }
    }

    @Override
    public boolean isValid(Object object)
    {
        try
        {
            return !required || field.get(object) != null;
        } catch (IllegalAccessException e)
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "JsonFieldLinker[Keys: " + Arrays.toString(getKeys()) + ", Required:" + required + "]";
    }
}

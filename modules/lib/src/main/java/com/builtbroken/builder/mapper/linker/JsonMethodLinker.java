package com.builtbroken.builder.mapper.linker;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.google.gson.JsonElement;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Robin Seifert on 2019-03-11.
 */
public class JsonMethodLinker extends JsonLinker<Object>
{

    private final Method method;

    public JsonMethodLinker(Method method, JsonObjectWiring mapping)
    {
        this(method, mapping.jsonFields(), mapping.objectType(), mapping.valuePrefix(), mapping.required());
    }

    public JsonMethodLinker(Method method, String[] names, String type, String prefix, boolean required)
    {
        super(names, type, prefix, required);
        this.method = method;
    }

    @Override
    public void link(Object object, JsonElement data, JsonObjectHandlerRegistry registry)
    {
        try
        {
            method.setAccessible(true);

            if (data.isJsonPrimitive() && data.getAsJsonPrimitive().isString())
            {
                final String key = prefix + data.getAsString();
                final IJsonObjectHandler handler = registry.getHandler(getType());
                if (handler != null)
                {
                    IJsonGeneratedObject objectToLink = handler.getObject(key);
                    if (objectToLink != null)
                    {
                        System.out.println("JsonMethodLinker: linked " + objectToLink);
                        method.invoke(object, objectToLink);
                    }
                    else
                    {
                        //TODO display warning? Might just leave this to validation
                        System.out.println("JsonMethodLinker: failed to locate object with name[" + key + "] from " + handler);
                    }
                }
                else
                {
                    //TODO display warning? Might just leave this to validation
                    System.out.println("JsonMethodLinker: failed to locate handler for type " + getType());
                }
            }
            else
            {
                throw new RuntimeException("JsonMethodLinker currently only supports using a string as a link key");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("JsonMethodMapper: Failed to link json object to " +
                    "method " + method.getName()
                    + "\n TYPE: " + type
                    + "\n OBJ: " + object
                    + "\n JSON: " + data,
                    e);
        }

    }

    @Override
    public void destroy()
    {

    }

    @Override
    public String toString()
    {
        return "JsonMethodLinker[Keys: " + Arrays.toString(getKeys()) + ", Required:" + required + "]";
    }
}

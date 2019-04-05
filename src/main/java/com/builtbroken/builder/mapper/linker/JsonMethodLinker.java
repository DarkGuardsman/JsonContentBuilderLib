package com.builtbroken.builder.mapper.linker;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.JsonObjectWiring;
import com.google.gson.JsonElement;

import java.lang.reflect.Method;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonMethodLinker extends JsonLinker<Object>
{

    private final Method method;

    public JsonMethodLinker(Method method, JsonObjectWiring mapping)
    {
        this(method, mapping.jsonFields(), mapping.objectType(), mapping.required());
    }

    public JsonMethodLinker(Method method, String[] names, String type, boolean required)
    {
        super(names, type, required);
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
                final String key = data.getAsString();
                final IJsonObjectHandler handler = registry.getHandler(getType());
                if (handler != null)
                {
                    IJsonGeneratedObject objectToLink = handler.getObject(key);
                    if (objectToLink != null)
                    {
                        method.invoke(object, objectToLink);
                    }
                    else
                    {
                        //TODO display warning? Might just leave this to validation
                    }
                }
                else
                {
                    //TODO display warning? Might just leave this to validation
                }
            }
            else
            {
                throw new RuntimeException("JsonFieldLinker currently only supports using a string as a link key");
            }
        } catch (Exception e)
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
}

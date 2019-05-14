package com.builtbroken.builder.mapper.linker;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonFieldLinker extends JsonLinker<Object>
{
    private final Field field;

    public JsonFieldLinker(Field field, JsonObjectWiring mapping)
    {
        this(field, mapping.jsonFields(), mapping.objectType(), mapping.required());
    }

    public JsonFieldLinker(Field field, String[] names, String type, boolean required)
    {
        super(names, type, required);
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
                final String key = data.getAsString();
                final IJsonObjectHandler handler = registry.getHandler(getType());
                if (handler != null)
                {
                    IJsonGeneratedObject objectToLink = handler.getObject(key);
                    if (objectToLink != null)
                    {
                        field.set(object, objectToLink);
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

        }
        catch (Exception e)
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
        }
        catch (IllegalAccessException e)
        {
            return false;
        }
    }
}

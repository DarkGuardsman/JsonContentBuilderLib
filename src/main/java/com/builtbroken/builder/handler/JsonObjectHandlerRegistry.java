package com.builtbroken.builder.handler;

import com.builtbroken.builder.data.GeneratedObject;

import java.util.HashMap;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonObjectHandlerRegistry
{

    private final HashMap<String, IJsonObjectHandler> handlers = new HashMap();

    /**
     * Called after the object is created and mapped
     *
     * @param object
     */
    public void onCreated(GeneratedObject object)
    {
        if (handlers.containsKey(object.type))
        {
            handlers.get(object.type).onCreated(object);
        }
        //TODO fire events
    }

    public IJsonObjectHandler getHandler(String type)
    {
        if (handlers.containsKey(type))
        {
            return handlers.get(type);
        }
        return null;
    }

}

package com.builtbroken.builder.handler;

import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonObjectHandlerRegistry
{

    private final HashMap<String, IJsonObjectHandler> handlers = new HashMap();
    private final HashMap<String, List<GeneratedObject>> unknownObjects = new HashMap(); //TODO clear on finish

    public IJsonObjectHandler createOrGetHandler(String type)
    {
        IJsonObjectHandler handler = getHandler(type);
        if (handler == null)
        {
            handler = new JsonObjectHandler(type.toLowerCase());
            handlers.put(type.toLowerCase(), handler);
        }
        return handler;
    }

    /**
     * Called after the object is created and mapped
     *
     * @param object
     */
    public void onCreated(GeneratedObject object)
    {
        final Object objectCreated = object.objectCreated;
        if (objectCreated instanceof IJsonGeneratedObject)
        {
            final IJsonGeneratedObject jsonGeneratedObject = (IJsonGeneratedObject) objectCreated;
            final String type = jsonGeneratedObject.getJsonType().toLowerCase();

            //Create handler if we have none
            createOrGetHandler(type).onCreated(jsonGeneratedObject);

            System.out.println("JsonObjectHandlerRegistry >> Registered: " + type + " >> " + jsonGeneratedObject);
        }
        else
        {
            final String type = object.type.toLowerCase();
            if (!unknownObjects.containsKey(type))
            {
                unknownObjects.put(type, new ArrayList());
            }
            unknownObjects.get(type).add(object);
        }
        //TODO fire events to allow hooking
    }

    public IJsonObjectHandler getHandler(String type)
    {
        if (handlers.containsKey(type.toLowerCase()))
        {
            return handlers.get(type.toLowerCase());
        }
        return null;
    }

    public void destroy()
    {
        handlers.values().forEach(handler -> handler.onRegistryDestroyed(this));
        handlers.clear();
        unknownObjects.clear();
    }
}

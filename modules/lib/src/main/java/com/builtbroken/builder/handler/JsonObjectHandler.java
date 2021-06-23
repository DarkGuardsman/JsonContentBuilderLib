package com.builtbroken.builder.handler;

import com.builtbroken.builder.data.IJsonGeneratedObject;

import java.util.HashMap;

/**
 * Created by Robin Seifert on 2019-03-11.
 */
public class JsonObjectHandler implements IJsonObjectHandler
{

    public final String id;
    private final HashMap<String, IJsonGeneratedObject> objectMap = new HashMap();

    public JsonObjectHandler(String id)
    {
        this.id = id;
    }

    @Override
    public void onCreated(IJsonGeneratedObject object)
    {
        if (id.equalsIgnoreCase(object.getJsonType()))
        {
            String uniqueID = object.getJsonUniqueID();
            if (uniqueID != null)
            {
                uniqueID = uniqueID.toLowerCase();
                if (!objectMap.containsKey(uniqueID))
                {
                    objectMap.put(uniqueID, object);
                }
                else
                {
                    //TODO error
                    System.out.println(this + ": Error, object was supplied where another object already has the unique ID. " + object);
                }
            }
            else
            {
                //TODO error
                System.out.println(this + ": Error, object was supplied that lacks a uniqueID. " + object);
            }
        }
        else
        {
            //TODO error
            System.out.println(this + ": Error, object was supplied that didn't match the handler's type. " + object);
        }
    }

    @Override
    public IJsonGeneratedObject getObject(String unqueId)
    {
        unqueId = unqueId.toLowerCase();
        if (objectMap.containsKey(unqueId))
        {
            return objectMap.get(unqueId);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "JsonObjectHandler[" + id + "]";
    }
}

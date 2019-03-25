package com.builtbroken.builder.handler;

import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;

import java.util.HashMap;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
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
            if (object.getJsonUniqueID() != null)
            {
                uniqueID = uniqueID.toLowerCase();
                if (!objectMap.containsKey(uniqueID))
                {
                    objectMap.put(uniqueID, object);
                }
                else
                {
                    //TODO error
                }
            }
            else
            {
                //TODO error
            }
        }
        else
        {
            //TODO error
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
}

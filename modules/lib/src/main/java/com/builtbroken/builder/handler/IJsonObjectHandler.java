package com.builtbroken.builder.handler;

import com.builtbroken.builder.data.IJsonGeneratedObject;

/**
 * Created by Robin Seifert on 2019-03-11.
 */
public interface IJsonObjectHandler<O extends IJsonGeneratedObject>
{

    /**
     * Called when an object is created matching
     * this handler's ID
     *
     * @param object - generated object, may not be mapped or contain all parts
     */
    void onCreated(IJsonGeneratedObject object);

    /**
     * Called to get an object by it's ID
     *
     * @param unqueId - ID of the object, match to lowercase
     * @return object if found, null if not
     */
    O getObject(String unqueId);

    /**
     * Called when the registry is destroyed
     * Use this to clear any memory references
     * that are only used by the registry.
     */
    default void onRegistryDestroyed(JsonObjectHandlerRegistry registry)
    {

    }
}

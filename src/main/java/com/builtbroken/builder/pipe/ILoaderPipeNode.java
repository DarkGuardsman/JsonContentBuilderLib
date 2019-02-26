package com.builtbroken.builder.pipe;

import com.google.gson.JsonObject;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public interface ILoaderPipeNode
{

    /**
     * Called as the object pass into this node when moving
     * down the pipe
     *
     * @param data          - current JSON data, previous nodes may have edited it
     * @param currentObject - current object, may be null if not built
     * @return current object, or new object if created
     */
    Object receive(JsonObject data, Object currentObject);

    /**
     * Unique ID of the node. Should be
     * provider:name
     * <p>
     * Ex: com.builtbroken:comment.cleaner
     * <p>
     * This is used mainly in error tracking
     *
     * @return id
     */
    String getUniqueID();
}

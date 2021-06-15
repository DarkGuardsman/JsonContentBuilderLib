package com.builtbroken.builder.pipe.nodes;

import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Single step in a pipe. Can be though of as an action.
 * <p>
 * When building your node and placing it in the pipe take time to understand
 * the input and output of other nodes. There is an expectation that a previous
 * node will generate the input needed by the next node/pipe. If your object
 * needs a JSON and the next needs an array. Then its important to match it or place
 * your node in a different location.
 * <p>
 * Optionally you can skip doing work on the passed in object but this is messy.
 * As it could indicate an error with the pipe if you received something unexpected.
 * <p>
 * Created by Robin Seifert on 2/26/19.
 */
public interface IPipeNode<O extends Object>
{

    /**
     * Called as the object pass into this node when moving
     * down the pipe
     *
     * @param data          - current JSON data, previous nodes may have edited it
     * @param currentObject - current object, may be null if not built or even a sub part of the JSON.
     * @param objectsOut    - any object generated in this phase, add current object if not consumed
     * @return current object, or new object if created
     */
    void receive(JsonElement data, O currentObject, Queue<Object> objectsOut);

    /**
     * Checks to see if this pipe node should process the object or JSON.
     * <p>
     * This allows nodes to control what flows into the node and
     * for nodes to be created that only handle some of the inputs.
     *
     * @param data          - current JSON data, previous nodes may have edited it
     * @param currentObject - current object, may be null if not built or even a sub part of the JSON.
     * @return {@Link NodeActionResult#CONTINUE} for {@link #receive(JsonElement, Object, Queue)} to be called
     * {@link NodeActionResult#REJECT} to fail out causing the process to stop, or {@link NodeActionResult#SKIP} to
     * continue without running {@link #receive(JsonElement, Object, Queue)}
     */
    NodeActionResult shouldReceive(JsonElement data, Object currentObject);

    /**
     * Called on pipe initialization to generate or reference
     * other systems.
     */
    default void init()
    {

    }

    /**
     * Called once everything is setup and loaded for the content building process.
     * Use this to validate that everything that this pipe needs is loaded. As well
     * that configurations has been setup properly.
     */
    default void onLoadComplete()
    {
    }


    /**
     * Type of node
     *
     * @return
     */
    NodeType getNodeType();

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

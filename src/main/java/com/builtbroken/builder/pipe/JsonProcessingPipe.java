package com.builtbroken.builder.pipe;

import com.builtbroken.builder.pipe.cleaning.PipeNodeCommentRemover;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class JsonProcessingPipe
{

    public static LinkedList<ILoaderPipeNode> cleanerNodes = new LinkedList();
    public static LinkedList<ILoaderPipeNode> builderNodes = new LinkedList();
    public static LinkedList<ILoaderPipeNode> mapperNodes = new LinkedList();

    static
    {
        cleanerNodes.add(new PipeNodeCommentRemover());
    }

    public static void handleFull(JsonObject object)
    {
        Object currentObject = processSet("json_cleaning", object, null, cleanerNodes);
        handleBuild(object, currentObject);
    }

    private static Object processSet(String name, JsonObject object, Object currentObject, LinkedList<ILoaderPipeNode> nodes)
    {

        final ListIterator<ILoaderPipeNode> iterator = nodes.listIterator();
        while (iterator.hasNext())
        {
            final ILoaderPipeNode node = iterator.next();
            try
            {
                currentObject = node.receive(object, currentObject);
            } catch (Exception e)
            {
                throw new RuntimeException("Unexpected error while processing node in pipeline[" + name + "], Node: " + node.getUniqueID() + " Class: " + node.getClass());
                //TODO add way for loader to provide more information about error
                //TODO format error to look nice in output log to better improve error handling by developers
            }
        }
        return currentObject;
    }

    public static void handleBuild(JsonObject object, Object currentObject)
    {
        processSet("building", object, currentObject, builderNodes);
        processSet("mapping", object, currentObject, mapperNodes);
    }
}

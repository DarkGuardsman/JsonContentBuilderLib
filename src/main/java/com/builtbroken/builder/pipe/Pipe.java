package com.builtbroken.builder.pipe;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.google.gson.JsonElement;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.function.BiConsumer;

/**
 * Series of pipe nodes that all match the same over all goal.
 * <p>
 * A pipe can be thought of a line of actions needed to be taken
 * in order to process an object. The reason for calling it a pipe
 * is that the flow can change directions and reverse to hit different
 * points in the pipe.
 * <p>
 * In theory an object should go from start to finish without ever doing this.
 * However, in cases were a single step produces several sub objects it
 * may be needed to re-run some steps.
 * <p>
 * When objects pass through the pipe its important to understand that
 * the type of object can change. You may start with a JSON and no object.
 * As you pass through the pipe the object may become a sub part of the
 * JSON, then turn into a data set of the JSON, then a generated object,
 * then a series of objects, finally ending as a complete object at the end.
 * <p>
 * Never assume that the input into your pipe is always going to be
 * the built object or null. Instead take time to understand the expected
 * output of the previous pipe and define your expected input. This is
 * where ordering of the pipes is important to receive
 * the right type of object.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class Pipe
{

    public final LinkedList<IPipeNode> nodes = new LinkedList();

    public final PipeLine pipeLine;
    public final String pipeName;

    private final Queue<Object> queueOut = new LinkedList();
    private final Queue<Object> queueIn = new LinkedList();

    protected BiConsumer<String, String> logger;

    public Pipe(PipeLine pipeLine, String pipeName)
    {
        this.pipeLine = pipeLine;
        this.pipeName = pipeName;
    }

    /**
     * Called to init the pipe, use
     * this to reference resources, objects,
     * and validate settings.
     */
    public void init()
    {
        getLogger().accept("Init", "Start");
        nodes.forEach(node -> node.init());
        getLogger().accept("Init", "End");
    }

    /**
     * Called to finish the loading phase, use
     * this to reference resources, objects,
     * and validate settings.
     */
    public void loadComplete()
    {
        getLogger().accept("Load", "Start");
        nodes.forEach(node -> node.onLoadComplete());
        getLogger().accept("Load", "End");
    }

    /**
     * Called after the previous pipe has completed to process a single object or json set
     *
     * @param jsonData      - json data that is left
     * @param currentObject - current object being handled, can be null and can also be the JSON broken into parts.
     * @param objectsOut    - additional objects generated that will get passed into the next pipe, add current object if not consumed
     */
    public void processSet(JsonElement jsonData, Object currentObject, Queue<Object> objectsOut)
    {
        getLogger().accept("Process", jsonData + "  " + currentObject);

        //Start
        queueIn.clear();
        queueOut.clear();
        queueIn.add(currentObject);

        //Loop nodes
        final ListIterator<IPipeNode> iterator = nodes.listIterator();
        while (iterator.hasNext())
        {
            final IPipeNode node = iterator.next();

            //Move objects to next to loop
            queueIn.addAll(queueOut);
            queueOut.clear();

            //Loop inputs
            while (queueIn.peek() != null)
            {
                handleNodeStep(node, jsonData, queueIn.poll(), queueOut);
            }
        }

        //Last run add everything
        objectsOut.addAll(queueOut);
        getLogger().accept("Process", "End " + objectsOut.size());
    }

    private void handleNodeStep(IPipeNode node, JsonElement jsonData, Object currentObject, Queue<Object> queueOut)
    {
        getLogger().accept("Node[" + node.getUniqueID() + "]", jsonData + " " + currentObject);
        try
        {
            node.receive(jsonData, currentObject, queueOut);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unexpected error while processing node in pipeline[" + pipeName + "], Node: " + node.getUniqueID() + " Class: " + node.getClass(), e);
            //TODO add way for loader to provide more information about error
            //TODO format error to look nice in output log to better improve error handling by developers
        }
    }

    public void addNode(IPipeNode node)
    {
        getLogger().accept("AddNode", node.toString());
        nodes.add(node);
    }

    public PipeLine getPipeLine()
    {
        return pipeLine;
    }

    public ContentLoader getLoader()
    {
        return pipeLine != null ? pipeLine.getLoader() : null;
    }

    public ConversionHandler getConverter()
    {
        return pipeLine != null ? pipeLine.getConverter() : null;
    }

    public BiConsumer<String, String> getLogger()
    {
        if (logger == null)
        {
            logger = (prefix, msg) ->
            {
                if (getPipeLine() != null)
                {
                    getPipeLine().getLogger().accept("Pipe[" + pipeName + "] >> " + prefix, msg);
                }
                else
                {
                    System.out.println("Pipe[" + pipeName + "]: " + prefix + " >> " + msg);
                }
            };
        }
        return logger;
    }
}

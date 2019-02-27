package com.builtbroken.builder.pipe;

import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

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
 * where ordering of the pipes is important in order to receive
 * the right type of object.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class Pipe
{

    public final LinkedList<IPipeNode> nodes = new LinkedList();
    public final String pipeName;
    public final boolean allowNullRuns;

    private Queue<Object> queueOut = new LinkedList();
    private Queue<Object> queueIn = new LinkedList();

    public Pipe(String pipeName, boolean allowNullRuns)
    {
        this.pipeName = pipeName;
        this.allowNullRuns = allowNullRuns;
    }

    /**
     * Called after the previous pipe has completed to process a single object or json set
     *
     * @param jsonData      - json data that is left
     * @param currentObject - current object being handled, can be null and can also be the JSON broken into parts.
     * @param objectsOut    - additional objects generated that will get passed into the next pipe, add current object if not consumed
     */
    public void processSet(JsonObject jsonData, Object currentObject, Queue<Object> objectsOut)
    {
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
            switchQueues();

            //If we have no objects run on null
            if (queueIn.isEmpty() && allowNullRuns)
            {
                queueIn.add(null);
            }

            //Loop inputs
            while (queueIn.peek() != null)
            {
                handleNodeStep(node, jsonData, queueIn.poll(), queueOut);
            }
        }

        //Last run add everything
        objectsOut.addAll(queueOut);
    }

    private void switchQueues()
    {
        Queue queue = queueIn;
        queueIn = queueOut;
        queueOut = queue;

        //Should already be empty if queue in was consumed fulled
        queueOut.clear();
    }

    private void handleNodeStep(IPipeNode node, JsonObject jsonData, Object currentObject, Queue<Object> queueOut)
    {
        try
        {
            node.receive(jsonData, currentObject, queueOut);
        } catch (Exception e)
        {
            throw new RuntimeException("Unexpected error while processing node in pipeline[" + pipeName + "], Node: " + node.getUniqueID() + " Class: " + node.getClass());
            //TODO add way for loader to provide more information about error
            //TODO format error to look nice in output log to better improve error handling by developers
        }
    }

    public void addNode(IPipeNode node)
    {
        nodes.add(node);
    }
}

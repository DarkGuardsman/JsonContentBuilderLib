package com.builtbroken.builder.pipe;

import com.builtbroken.builder.References;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeCommentRemover;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.function.Function;

/**
 * Instance of a series of pipes that are connected together.
 * <p>
 * Each pipe can have sub nodes and loop back on itself. An example
 * of this would be something going through the cleaner phase. Then
 * when it hits the builder phase it splits out into 10 sub JSONs. Each
 * of these sub JSONs would then loop back on the pipe to check if it
 * needs to split again before moving through to the rest of the builder
 * phase.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2/27/19.
 */
public class PipeLine
{

    public final LinkedList<Pipe> pipes = new LinkedList();
    public final HashMap<String, Pipe> id_to_pipe = new HashMap();

    public static PipeLine newDefault()
    {
        PipeLine handler = new PipeLine();

        //Setup cleaner
        Pipe jsonPrepPipe = new Pipe(References.PIPE_JSON, true);
        jsonPrepPipe.addNode(new PipeNodeCommentRemover());
        handler.pipes.add(jsonPrepPipe);

        //Setup builder
        Pipe builderPipe = new Pipe(References.PIPE_BUILDER, false);
        handler.pipes.add(builderPipe);

        //Setup mapper
        Pipe mapperPipe = new Pipe(References.PIPE_MAPPER, false);
        handler.pipes.add(mapperPipe);

        return handler;
    }

    public void init()
    {
        //TODO sort out pipes and nodes so they run in order
    }

    /**
     * Called to add a node to the pipe
     *
     * @param key  - id of the pipe
     * @param node - node to add
     */
    public void add(String key, IPipeNode node)
    {
        if (id_to_pipe.containsKey(key.toLowerCase()))
        {
            id_to_pipe.get(key.toLowerCase()).addNode(node);
        }
    }

    /**
     * Called to handle a JSON and turn it into objects
     *
     * @param jsonData       - current JSON object being processed
     * @param shouldSkipPipe - function to allow skipping some pipes, this useful if something was generated that needs to go through
     *                       an entire pipe from the start but shouldn't be run through the previous pipes.
     * @return
     */
    public List<Object> handle(JsonObject jsonData, Function<Pipe, Boolean> shouldSkipPipe) //TODO need a set return type other than object, something that is <Type, Data>
    {
        final List<Object> builtObjects = new ArrayList();
        Queue<Object> currentIN = new LinkedList();
        Queue<Object> currentOut = new LinkedList();
        for (Pipe pipe : pipes)
        {
            //Check if we should run this pipe
            if (shouldSkipPipe == null || !shouldSkipPipe.apply(pipe))
            {
                //Add previous out to next pipe input
                currentIN.addAll(currentOut);
                currentOut.clear();

                //Add null if we want null runs
                if (currentIN.isEmpty() && pipe.allowNullRuns)
                {
                    currentIN.add(null);
                }

                //loop inputs
                while (currentIN.peek() != null)
                {
                    pipe.processSet(jsonData, currentIN.poll(), currentOut);
                }
            }
        }
        return builtObjects;
    }
}

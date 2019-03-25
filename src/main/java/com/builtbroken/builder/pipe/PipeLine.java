package com.builtbroken.builder.pipe;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeAutoWire;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeFieldHandler;
import com.builtbroken.builder.pipe.nodes.building.PipeNodeObjectCreator;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeCommentRemover;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeJsonSplitter;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeMappingValidator;
import com.google.gson.JsonElement;

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

    public ContentLoader contentLoader;

    public static PipeLine newDefault()
    {
        PipeLine handler = new PipeLine();

        //Setup cleaner
        Pipe jsonPrepPipe = new Pipe(handler,  ContentBuilderRefs.PIPE_JSON, true);
        jsonPrepPipe.addNode(new PipeNodeCommentRemover());
        jsonPrepPipe.addNode(new PipeNodeJsonSplitter());
        handler.pipes.add(jsonPrepPipe);

        //Setup builder
        Pipe builderPipe = new Pipe(handler,  ContentBuilderRefs.PIPE_BUILDER, false);
        builderPipe.addNode(new PipeNodeObjectCreator(builderPipe));
        handler.pipes.add(builderPipe);

        //Setup mapper
        Pipe mapperPipe = new Pipe(handler,  ContentBuilderRefs.PIPE_MAPPER, false);
        builderPipe.addNode(new PipeNodeFieldHandler(mapperPipe));
        builderPipe.addNode(new PipeNodeAutoWire(mapperPipe));
        builderPipe.addNode(new PipeNodeMappingValidator(mapperPipe));
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
    public List<Object> handle(JsonElement jsonData, Function<Pipe, Boolean> shouldSkipPipe) //TODO need a set return type other than object, something that is <Type, Data>
    {
        final List<Object> builtObjects = new ArrayList();
        final Queue<Object> currentIN = new LinkedList();
        final Queue<Object> currentOut = new LinkedList();

        //Add JSON to first input
        currentIN.add(jsonData);

        for (Pipe pipe : pipes)
        {
            //Check if we should run this pipe
            if (shouldSkipPipe == null || !shouldSkipPipe.apply(pipe))
            {
                //Add previous out to next pipe input
                currentIN.addAll(currentOut);
                currentOut.clear();

                //loop inputs
                while (currentIN.peek() != null)
                {
                    pipe.processSet(jsonData, currentIN.poll(), currentOut);
                }
            }
        }

        //Output
        builtObjects.addAll(currentOut);
        builtObjects.removeIf(o -> o == null);
        return builtObjects;
    }

    public ContentLoader getLoader()
    {
        return contentLoader;
    }

    public ConversionHandler getConverter()
    {
        return contentLoader != null ? contentLoader.conversionHandler : null;
    }

    /**
     * Called to destroy the pipe and empty all memory connections
     */
    public void destroy()
    {
        pipes .clear();
        id_to_pipe.clear();
    }
}

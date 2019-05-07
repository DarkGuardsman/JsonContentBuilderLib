package com.builtbroken.builder.pipe;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.pipe.nodes.building.PipeNodeObjectReg;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeAutoWire;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeFieldHandler;
import com.builtbroken.builder.pipe.nodes.building.PipeNodeObjectCreator;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeCommentRemover;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeJsonSplitter;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeMappingValidator;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Handles the process of taking in JSON and converting them into completed objects.
 * <p>
 * Called a pipe due to the simi-linear nature of the flow of progression. It starts
 * at the beginning as a raw JSON object tree. Then steps through the first pipe
 * which cleans the JSON and formats it for processing. The next pipe then turns
 * the JSON into an object or series of objects. The last pipe maps all data to
 * the object, registers it to handlers, and wires the objects together.
 * <p>
 * In each of these pipes can exist a series of steps known as nodes. Each node
 * represents a single step that can be completed on its own. Working together
 * these nodes complete the objective of each pipe. With all pipes working together
 * to meet the goal of the pipe line.
 * <p>
 * PipeLine -> Steps to fully complete the json
 * Pipe -> Series of steps to finish 1 section
 * Node -> Single step
 * <p>
 * As a note, Each pipe can have sub nodes and loop back on itself. An example
 * of this would be something going through the cleaner phase. Then
 * when it hits the builder phase it splits out into 10 sub JSONs. Each
 * of these sub JSONs may loop back on the pipe to check if it
 * needs to split again before moving through to the rest of the builder
 * phase.
 * <p>
 * The current version is not setup this way but it is possible. Hence the reason
 * for having pipes that are separated from each other. This allows data to flow
 * back through the pipe before moving on again. It also allows new pipes to be
 * integrated into the process. Including testing tools, debug tools, or new processes
 * to integrate with existing complex systems.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2/27/19.
 */
public class PipeLine
{

    /**
     * Pipes in the line, ordered
     */
    public final LinkedList<Pipe> pipes = new LinkedList();
    /**
     * ID to pipe, used to lookup pipes for adding nodes
     */
    public final HashMap<String, Pipe> id_to_pipe = new HashMap();

    public ContentLoader contentLoader;

    /**
     * Creates a default pipe line. It is recommended to use
     * this unless something custom is needed outside the scope of the system.
     *
     * @return default line
     */
    public static PipeLine newDefault()
    {
        PipeLine handler = new PipeLine();

        //Setup cleaner
        Pipe jsonPrepPipe = new Pipe(handler, ContentBuilderRefs.PIPE_JSON);
        jsonPrepPipe.addNode(new PipeNodeCommentRemover()); //cleanup
        jsonPrepPipe.addNode(new PipeNodeJsonSplitter()); //breakdown
        handler.pipes.add(jsonPrepPipe);

        //Setup builder
        Pipe builderPipe = new Pipe(handler, ContentBuilderRefs.PIPE_BUILDER);
        builderPipe.addNode(new PipeNodeObjectCreator(builderPipe)); //Create json
        builderPipe.addNode(new PipeNodeObjectReg(builderPipe)); //register to handlers
        handler.pipes.add(builderPipe);

        //Setup mapper
        Pipe mapperPipe = new Pipe(handler, ContentBuilderRefs.PIPE_MAPPER);
        mapperPipe.addNode(new PipeNodeFieldHandler(mapperPipe)); //map fields
        mapperPipe.addNode(new PipeNodeAutoWire(mapperPipe)); //wire objects
        mapperPipe.addNode(new PipeNodeMappingValidator(mapperPipe)); //validate
        handler.pipes.add(mapperPipe);

        return handler;
    }

    /**
     * Called to init the pipe line, use
     * this to reference resources, objects,
     * and validate settings.
     */
    public void init()
    {
        //TODO sort out pipes and nodes so they run in order
        pipes.removeIf(pipe -> pipe == null);
        pipes.forEach(pipe -> pipe.init());
    }

    /**
     * Called to finish loading phase, use
     * this to reference resources, objects,
     * and validate settings.
     */
    public void loadComplete()
    {
        pipes.forEach(pipe -> pipe.loadComplete());
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
    @Nonnull
    public List<Object> handle(@Nonnull JsonElement jsonData, @Nullable Function<Pipe, Boolean> shouldSkipPipe) //TODO need a set return type other than object, something that is <Type, Data>
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
        pipes.clear();
        id_to_pipe.clear();
    }
}

package com.builtbroken.builder.pipe.nodes.mapping;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.PipeNode;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Handles how to convert from stored JSON data into fields
 * Created by Dark(DarkGuardsman, Robert) on 2019-02-27.
 */
public class PipeNodeDataMapper extends PipeNode
{
    public PipeNodeDataMapper(Pipe pipe)
    {
        super(pipe, NodeType.MAPPER, ContentBuilderRefs.PIPE_MAPPER_FIELDS);
    }

    @Override
    public void receive(JsonElement data, Object currentObject, Queue<Object> objectsOut)
    {
        if (currentObject instanceof GeneratedObject)
        {
            final GeneratedObject generatedObjectData = (GeneratedObject) currentObject;

            //Map
            getContentLoader().jsonMappingHandler.map(generatedObjectData.type, generatedObjectData.objectCreated,
                    generatedObjectData.jsonUsed, false);

            //Pass to next
            objectsOut.add(generatedObjectData);
        }
        else
        {
            //TODO throw error
        }
    }
}

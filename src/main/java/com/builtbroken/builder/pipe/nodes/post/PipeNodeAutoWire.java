package com.builtbroken.builder.pipe.nodes.post;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.PipeNode;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-02-27.
 */
public class PipeNodeAutoWire extends PipeNode
{

    public PipeNodeAutoWire(Pipe pipe)
    {
        super(pipe, NodeType.POST, ContentBuilderRefs.PIPE_POST_WIRING);
    }

    @Override
    public void receive(JsonElement data, Object currentObject, Queue<Object> objectsOut)
    {
        if (currentObject instanceof GeneratedObject)
        {
            final GeneratedObject generatedObjectData = (GeneratedObject) currentObject;

            //Map
            getContentLoader().jsonMappingHandler.map(generatedObjectData.type, generatedObjectData.objectCreated,
                    generatedObjectData.jsonUsed, true);


            //Pass to next
            objectsOut.add(generatedObjectData);
        }
        else
        {
            //TODO throw error
        }
    }
}
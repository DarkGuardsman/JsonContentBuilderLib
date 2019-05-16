package com.builtbroken.builder.pipe.nodes.building;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.PipeNode;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Handles placing objects into handlers
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-05.
 */
public class PipeNodeObjectReg extends PipeNode
{
    public PipeNodeObjectReg(Pipe pipe)
    {
        super(pipe, NodeType.BUILDER, ContentBuilderRefs.PIPE_BUILDER_REG);
    }

    @Override
    public void receive(JsonElement data, Object currentObject, Queue<Object> objectsOut)
    {
        //Can only handle json objects
        if (currentObject instanceof GeneratedObject)
        {
            final GeneratedObject generatedObject = (GeneratedObject) currentObject;
            getContentLoader().jsonObjectHandlerRegistry.onCreated(generatedObject);
            //TODO fire some events

            //Pass to next
            objectsOut.add(generatedObject);
        }
        else
        {
            System.out.println("PipeNodeObjectReg: Error, Input into object creator needs to be a GeneratedObject. Input: " + currentObject);
            //TODO throw error, only if strict mode is enabled
        }
    }

    @Override
    public void onLoadComplete()
    {
        //Validate that we have our needed components
        if (getContentLoader() == null || getContentLoader().jsonObjectHandlerRegistry == null)
        {
            throw new RuntimeException("PipeNodeObjectReg: A content loader and object handler are required to register objects to handlers.");
        }
    }
}

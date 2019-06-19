package com.builtbroken.builder.pipe.nodes.mapping;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNode;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNodeGenObject;
import com.google.gson.JsonElement;

/**
 * Handles placing objects into handlers
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-05.
 */
public class PipeNodeObjectReg extends PipeNodeGenObject
{
    public PipeNodeObjectReg(Pipe pipe)
    {
        super(pipe, NodeType.MAPPER, ContentBuilderRefs.PIPE_MAPPER_OBJECT_REG);
    }

    @Override
    public void receive(GeneratedObject generatedObject)
    {
        getContentLoader().jsonObjectHandlerRegistry.onCreated(generatedObject);
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

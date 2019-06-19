package com.builtbroken.builder.pipe.nodes.post;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNode;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNodeGenObject;
import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-02-27.
 */
public class PipeNodeAutoWire extends PipeNodeGenObject
{

    public PipeNodeAutoWire(Pipe pipe)
    {
        super(pipe, NodeType.POST, ContentBuilderRefs.PIPE_POST_WIRING);
    }

    @Override
    public void receive(GeneratedObject generatedObjectData)
    {
        //Map
        getContentLoader().jsonMappingHandler.map(generatedObjectData.type, generatedObjectData.objectCreated,
                generatedObjectData.jsonUsed, true);
    }
}

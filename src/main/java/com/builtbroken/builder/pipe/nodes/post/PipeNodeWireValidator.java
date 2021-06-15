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
 * Created by Robin Seifert on 2019-03-15.
 */
public class PipeNodeWireValidator extends PipeNodeGenObject
{

    public PipeNodeWireValidator(Pipe pipe)
    {
        super(pipe, NodeType.POST, ContentBuilderRefs.PIPE_POST_WIRING_VALIDATION);
    }

    @Override
    public void receive(GeneratedObject generatedObjectData)
    {
        //Check validation of mappings
        getContentLoader().jsonMappingHandler.validate(generatedObjectData.type, generatedObjectData.objectCreated, true);
    }
}
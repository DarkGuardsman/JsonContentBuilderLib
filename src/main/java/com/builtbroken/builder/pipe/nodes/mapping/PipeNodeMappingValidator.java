package com.builtbroken.builder.pipe.nodes.mapping;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.ISimpleDataValidation;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNode;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNodeGenObject;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNodeSimple;
import com.google.gson.JsonElement;

/**
 * Created by Robin Seifert on 2019-03-15.
 */
public class PipeNodeMappingValidator extends PipeNodeGenObject
{

    public PipeNodeMappingValidator(Pipe pipe)
    {
        super(pipe, NodeType.MAPPER, ContentBuilderRefs.PIPE_MAPPER_VALIDATION);
    }

    @Override
    public void receive(GeneratedObject generatedObjectData)
    {
        //Check validation of mappings
        getContentLoader().jsonMappingHandler.validate(generatedObjectData.type, generatedObjectData.objectCreated, false);
    }
}
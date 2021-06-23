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
 * Handles how to convert from stored JSON data into fields
 * Created by Robin Seifert on 2019-02-27.
 */
public class PipeNodeDataMapper extends PipeNodeGenObject
{

    public PipeNodeDataMapper(Pipe pipe)
    {
        super(pipe, NodeType.MAPPER, ContentBuilderRefs.PIPE_MAPPER_FIELDS);
    }

    @Override
    public void receive(GeneratedObject generatedObjectData)
    {
        //Map
        getContentLoader().jsonMappingHandler.map(generatedObjectData.type, generatedObjectData.objectCreated,
                generatedObjectData.jsonUsed, false);
    }
}

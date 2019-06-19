package com.builtbroken.builder.pipe.nodes.prefab;

import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-19.
 */
public abstract class PipeNodeGenObject extends PipeNode<GeneratedObject>
{

    protected PipeNodeGenObject(Pipe pipe, NodeType type, String id)
    {
        super(pipe, type, id);
    }

    @Override
    public void receive(JsonElement data, GeneratedObject currentObject, Queue<Object> objectsOut)
    {
        receive(currentObject);
        objectsOut.add(currentObject);
    }

    public void receive(GeneratedObject currentObject)
    {

    }

    @Override
    public NodeActionResult shouldReceive(JsonElement data, Object currentObject)
    {
        return currentObject instanceof GeneratedObject ? NodeActionResult.CONTINUE : NodeActionResult.REJECT;
    }
}

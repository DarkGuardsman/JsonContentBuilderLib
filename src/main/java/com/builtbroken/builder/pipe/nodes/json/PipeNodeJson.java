package com.builtbroken.builder.pipe.nodes.json;

import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNode;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-19.
 */
public abstract class PipeNodeJson extends PipeNode<JsonElement>
{

    protected PipeNodeJson(Pipe pipe, NodeType type, String id)
    {
        super(pipe, type, id);
    }

    @Override
    public void receive(JsonElement data, JsonElement currentObject, Queue<Object> objectsOut)
    {
        receive(currentObject);
        objectsOut.add(currentObject);
    }

    public void receive(JsonElement currentObject)
    {

    }

    @Override
    public NodeActionResult shouldReceive(JsonElement data, Object currentObject)
    {
        return currentObject instanceof JsonElement ? NodeActionResult.CONTINUE : NodeActionResult.REJECT;
    }
}

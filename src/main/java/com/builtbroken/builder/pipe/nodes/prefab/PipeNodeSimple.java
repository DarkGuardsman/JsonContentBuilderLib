package com.builtbroken.builder.pipe.nodes.prefab;

import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-19.
 */
public abstract class PipeNodeSimple<I extends Object, O extends Object> extends PipeNode<I>
{
    protected PipeNodeSimple(Pipe pipe, NodeType type, String id)
    {
        super(pipe, type, id);
    }

    @Override
    public void receive(JsonElement data, I object, Queue<Object> objectsOut)
    {
        receive(convert(object));
        objectsOut.add(object);
    }

    protected abstract O convert(I object);

    protected abstract void receive(O object);
}

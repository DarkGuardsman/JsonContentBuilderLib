package com.builtbroken.builder.pipe.nodes.post;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.ISimpleDataValidation;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNode;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNodeSimple;
import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-16.
 */
public class PipeNodePostValidator extends PipeNodeSimple<GeneratedObject, ISimpleDataValidation>
{

    public PipeNodePostValidator(Pipe pipe)
    {
        super(pipe, NodeType.POST, ContentBuilderRefs.PIPE_POST_VALIDATION);
    }

    @Override
    public void receive(ISimpleDataValidation jsonGeneratedObject)
    {
        if (!jsonGeneratedObject.isValid())
        {
            throw new RuntimeException("PipeNodePostValidator: object is invalid and may not have built correctly. "
                    + "Object: " + jsonGeneratedObject);
        }
    }

    @Override
    protected ISimpleDataValidation convert(GeneratedObject object)
    {
        return (ISimpleDataValidation) object.objectCreated;
    }

    @Override
    public NodeActionResult shouldReceive(JsonElement data, Object currentObject)
    {
        if (currentObject instanceof GeneratedObject)
        {
            if (((GeneratedObject) currentObject).objectCreated instanceof ISimpleDataValidation)
            {
                return NodeActionResult.CONTINUE;
            }
            return NodeActionResult.SKIP;
        }
        return NodeActionResult.REJECT;
    }
}
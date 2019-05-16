package com.builtbroken.builder.pipe.nodes.post;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.data.ISimpleDataValidation;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.PipeNode;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-16.
 */
public class PipeNodePostValidator extends PipeNode
{

    public PipeNodePostValidator(Pipe pipe)
    {
        super(pipe, NodeType.POST, ContentBuilderRefs.PIPE_POST_VALIDATION);
    }

    @Override
    public void receive(JsonElement data, Object currentObject, Queue<Object> objectsOut)
    {
        if (currentObject instanceof GeneratedObject)
        {
            final GeneratedObject generatedObjectData = (GeneratedObject) currentObject;
            if (generatedObjectData.objectCreated instanceof ISimpleDataValidation)
            {
                ISimpleDataValidation jsonGeneratedObject = (ISimpleDataValidation) generatedObjectData.objectCreated;
                if (!jsonGeneratedObject.isValid())
                {
                    throw new RuntimeException("PipeNodePostValidator: object is invalid and may not have built correctly. "
                            + "Object: " + jsonGeneratedObject);
                }
            }
            else
            {
                //TODO provide alt option to validate object
            }

            //Pass to next
            objectsOut.add(generatedObjectData);
        }
        else
        {
            //TODO throw error
        }
    }
}
package com.builtbroken.builder.pipe.nodes.building;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Queue;

/**
 * Handles how to convert from JSON data into objects. Does
 * not actually map the data to the fields.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-02-27.
 */
public class PipeNodeObjectCreator implements IPipeNode
{

    @Override
    public void receive(JsonElement data, Object currentObject, Queue<Object> objectsOut)
    {
        if (currentObject instanceof JsonObject)
        {
            final JsonObject jsonObject = (JsonObject) currentObject;
            if(jsonObject.has("type"))
            {

            }
            else
            {
                System.out.println("Error: Input JsonObject needs to define a type to use this builder. Input: " + currentObject);
                //TODO throw error
            }
        }
        else
        {
            System.out.println("Error: Input into object creator needs to be a JsonObject. Input: " + currentObject);
            //TODO throw error
        }
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.BUILDER;
    }

    @Override
    public String getUniqueID()
    {
        return ContentBuilderRefs.PIPE_OBJECT_CREATOR;
    }
}

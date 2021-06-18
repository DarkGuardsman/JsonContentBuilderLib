package com.builtbroken.builder.pipe.nodes.json;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Queue;

/**
 * Splits JSON objects into smaller parts
 * <p>
 * Created by Robin Seifert on 2019-02-27.
 */
public class PipeNodeJsonSplitter implements IPipeNode<JsonArray>
{
    @Override
    public void receive(JsonElement data, JsonArray currentObject, Queue<Object> objectsOut)
    {
        for (JsonElement element : currentObject)
        {
            if (element.isJsonObject())
            {
                objectsOut.add(JsonHelpers.deepCopy(element.getAsJsonObject()));
            }
            else
            {
                throw new IllegalArgumentException("PipeNodeJsonSplitter: Can only split by objects in an array. Primitives and arrays are not considered there own objects for the pipe.");
            }
        }
    }

    @Override
    public NodeActionResult shouldReceive(JsonElement data, Object currentObject)
    {
        if (currentObject instanceof JsonArray)
        {
            return NodeActionResult.CONTINUE;
        }
        return NodeActionResult.SKIP;
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.JSON_EDITOR;
    }

    @Override
    public String getUniqueID()
    {
        return ContentBuilderRefs.PIPE_JSON_SPLITTER;
    }
}

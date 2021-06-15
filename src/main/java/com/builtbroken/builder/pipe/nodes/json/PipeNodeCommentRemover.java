package com.builtbroken.builder.pipe.nodes.json;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.IPipeNode;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Created by Robin Seifert on 2/26/19.
 */
public class PipeNodeCommentRemover extends PipeNodeJson
{
    public PipeNodeCommentRemover(Pipe pipe)
    {
        super(pipe, NodeType.JSON_EDITOR, ContentBuilderRefs.PIPE_COMMENT_REMOVER);
    }

    @Override
    public void receive(JsonElement currentObject)
    {
        if (currentObject.isJsonObject())
        {
            cleanObject(currentObject.getAsJsonObject(), 0);
        }
        else if (currentObject.isJsonArray())
        {
            cleanArray(currentObject.getAsJsonArray(), 0);
        }
    }

    private void cleanArray(JsonArray array, int depth)
    {
        final Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext())
        {
            JsonElement next = iterator.next();
            if (next.isJsonObject())
            {
                cleanObject(next.getAsJsonObject(), depth++);
            }
            else if (next.isJsonArray())
            {
                cleanArray(array, depth++);
            }
        }
    }

    private void cleanObject(JsonObject data, int depth)
    {
        final List<String> ids = new ArrayList(data.keySet());
        for (String id : ids)
        {
            //Remove comments
            if (isComment(id, data.get(id)))
            {
                data.remove(id);
            }
            else
            {
                //Check if is an object that needs cleaned
                final JsonElement elementToCheck = data.get(id);
                if (elementToCheck.isJsonObject())
                {
                    cleanObject(elementToCheck.getAsJsonObject(), depth++);
                }
                else if (elementToCheck.isJsonArray())
                {
                    cleanArray(elementToCheck.getAsJsonArray(), depth++);
                }
            }
        }
    }

    private boolean isComment(String string, JsonElement element)
    {
        return string.startsWith("_") && element.isJsonPrimitive();
        //TODO create a list of prefixes that count as comments
        //TODO create a way to disable removal in some cases (file, path, depth, etc)
    }
}

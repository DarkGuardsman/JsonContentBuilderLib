package com.builtbroken.builder.pipe.nodes.building;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeActionResult;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.json.JsonHelpers;
import com.builtbroken.builder.pipe.nodes.prefab.PipeNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Queue;

/**
 * Handles how to convert from JSON data into objects. Does
 * not actually map the data to the fields.
 * <p>
 * Created by Robin Seifert on 2019-02-27.
 */
public class PipeNodeObjectCreator extends PipeNode<JsonObject>
{

    /**
     * Key to use for finding what type an object is for the conversion. Change this to match your json format.
     * <p>
     * If you plan to support more than 1 field for types. Create a wrapper Pipe Node that stores more than
     * 1 version of this class or create a completely new pipe node that maps several fields to the type.
     */
    public String type_key = ContentBuilderRefs.JSON_TYPE; //TODO rename to 'template' as type for template != registry type

    public PipeNodeObjectCreator(Pipe pipe)
    {
        super(pipe, NodeType.BUILDER, ContentBuilderRefs.PIPE_BUILDER_CREATION);
    }

    @Override
    public void receive(JsonElement data, JsonObject jsonObject, Queue<Object> objectsOut)
    {
        //Make sure we have the type field
        if (jsonObject.has(type_key))
        {
            final ConversionHandler handler = getConverter();
            final String type = jsonObject.getAsJsonPrimitive(type_key).getAsString();

            //Convert object
            final Object object = handler.fromJson(type, jsonObject, null); //TODO maybe include a few args like version if found
            if (object != null)
            {
                objectsOut.add(new GeneratedObject(type, object, JsonHelpers.deepCopy(jsonObject)));
            }
            else
            {
                System.err.println("PipeNodeObjectCreator: Error, Failed to convert object. Input: " + jsonObject);
                //TODO throw error, only if strict mode is enabled
            }
        }
        else
        {
            System.err.println("PipeNodeObjectCreator: Error, Input JsonObject needs to define a type to use this builder. Input: " + jsonObject);
            //TODO throw error, only if strict mode is enabled
        }
    }

    @Override
    public NodeActionResult shouldReceive(JsonElement data, Object currentObject)
    {
        return currentObject instanceof JsonObject ? NodeActionResult.CONTINUE : NodeActionResult.REJECT;
    }

    @Override
    public void onLoadComplete()
    {
        //Validate that we have our needed components
        if (getContentLoader() == null || getConverter() == null)
        {
            throw new RuntimeException("PipeNodeObjectCreator: A content loader and converter are required to build objects from JSON for this pipe node.");
        }
    }
}

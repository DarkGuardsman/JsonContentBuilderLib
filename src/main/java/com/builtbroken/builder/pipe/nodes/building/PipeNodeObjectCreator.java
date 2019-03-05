package com.builtbroken.builder.pipe.nodes.building;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.nodes.NodeType;
import com.builtbroken.builder.pipe.nodes.PipeNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Queue;

/**
 * Handles how to convert from JSON data into objects. Does
 * not actually map the data to the fields.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-02-27.
 */
public class PipeNodeObjectCreator extends PipeNode
{

    /**
     * Key to use for finding what type an object is for the conversion. Change this to match your json format.
     * <p>
     * If you plan to support more than 1 field for types. Create a wrapper Pipe Node that stores more than
     * 1 version of this class or create a completely new pipe node that maps several fields to the type.
     */
    public String type_key = "type";

    public PipeNodeObjectCreator(Pipe pipe)
    {
        super(pipe, NodeType.BUILDER, ContentBuilderRefs.PIPE_OBJECT_CREATOR);
    }

    @Override
    public void receive(JsonElement data, Object currentObject, Queue<Object> objectsOut)
    {
        //Can only handle json objects
        if (currentObject instanceof JsonObject)
        {
            final JsonObject jsonObject = (JsonObject) currentObject;

            //Make sure we have the type field
            if (jsonObject.has(type_key))
            {
                final ConversionHandler handler = getConverter();
                final String type = jsonObject.getAsJsonPrimitive(type_key).getAsString();

                //Convert object
                final Object object = handler.fromJson(type, jsonObject, null); //TODO maybe include a few args like version if found
                if (object != null)
                {
                    objectsOut.add(new GeneratedObject(type, object, jsonObject.deepCopy()));
                }
                else
                {
                    System.out.println("PipeNodeObjectCreator: Error, Failed to convert object. Input: " + currentObject);
                    //TODO throw error, only if strict mode is enabled
                }
            }
            else
            {
                System.out.println("PipeNodeObjectCreator: Error, Input JsonObject needs to define a type to use this builder. Input: " + currentObject);
                //TODO throw error, only if strict mode is enabled
            }
        }
        else
        {
            System.out.println("PipeNodeObjectCreator: Error, Input into object creator needs to be a JsonObject. Input: " + currentObject);
            //TODO throw error, only if strict mode is enabled
        }
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

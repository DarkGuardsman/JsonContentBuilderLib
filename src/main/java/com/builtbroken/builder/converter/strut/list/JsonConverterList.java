package com.builtbroken.builder.converter.strut.list;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Simple map converter, more complex version will be added later
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-06.
 */
public class JsonConverterList extends JsonConverter<Collection>
{
    private ConversionHandler handler;

    public JsonConverterList()
    {
        super("java:" + ConverterRefs.LIST, ConverterRefs.LIST);
    }

    @Override
    public void onRegistered(ConversionHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof Collection;
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }


    @Override
    public JsonArray toJson(Collection objects, String[] args)
    {
        JsonArray array = new JsonArray();
        if (args == null || args.length == 0)
        {
            throw new IllegalArgumentException("JsonConverterList: args are required to define conversion type");
        }

        //Get data
        final String type = args[0].toLowerCase();
        final String[] sub_args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null;

        for (Object object : objects)
        {
            //TODO consider pulling additional data in case we are storing subtypes so we can recover/save properly
            final JsonElement element = handler.toJson(type, object, sub_args);
            if (element != null)
            {
                array.add(element);
            }
            else
            {
                throw new RuntimeException("JsonConverterList; Failed to convert object to json using "
                        + " TYPE: " + type
                        + " ARGS: " + args
                        + " OBJECT: " + object);
            }
        }

        return array;
    }

    @Override
    public List fromJson(JsonElement data, String[] args)
    {
        if (args == null || args.length == 0)
        {
            throw new IllegalArgumentException("JsonConverterList: args are required to define conversion type");
        }
        else if (!data.isJsonArray())
        {
            throw new IllegalArgumentException("JsonConverterList: can only convert JSON arrays to list format");
        }

        //Get data
        final String type = args[0].toLowerCase();
        final String[] sub_args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null;

        //Store as list since we will do special handling via add instead of creating the field
        final List list = new ArrayList();

        for (JsonElement element : data.getAsJsonArray())
        {
            final Object object = handler.fromJson(type, element, sub_args);
            if (object != null)
            {
                list.add(object);
            }
            else
            {
                throw new RuntimeException("JsonConverterList; Failed to convert json to object using "
                        + " TYPE: " + type
                        + " ARGS: " + args
                        + " JSON: " + element);
            }
        }

        return list;
    }

}

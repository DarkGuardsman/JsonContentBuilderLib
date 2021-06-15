package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Arrays;

/**
 * Created by Robin Seifert on 2019-03-05.
 */
public class JsonConverterArray extends JsonConverter
{

    private ConversionHandler handler;

    public JsonConverterArray()
    {
        super("java:array.object", "array", "array.object", "[]", "object[]");
    }

    @Override
    public void onRegistered(ConversionHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object.getClass().isArray() && !object.getClass().getComponentType().isPrimitive();
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input, String[] args)
    {
        if (args == null || args.length >= 1)
        {
            throw new IllegalArgumentException("JsonConverterArray: args are required to define conversion type");
        }

        //Get data
        final String type = args[0].toLowerCase();
        final String[] type_args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        final Object[] array = (Object[]) input;
        for (Object object : array)
        {
            final JsonElement element = handler.toJson(type, object, type_args);
            if (element != null)
            {
                jsonArray.add(element);
            }
            else
            {
                System.out.println("JsoNConverterArray: Error converting [" + object + "] to json using data[type=" + type + ", args=" + type_args + "]");
                //TODO throw error if strict mode
            }
        }
        return jsonArray;
    }

    @Override
    public Object fromJson(JsonElement input, String[] args)
    {
        if (args == null || args.length >= 1)
        {
            throw new IllegalArgumentException("JsonConverterArray: args are required to define conversion type");
        }

        //Get data
        final String type = args[0].toLowerCase();
        final String[] type_args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null;

        //Generate array
        final JsonArray jsonArray = input.getAsJsonArray();
        final Object[] array = new Object[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++)
        {
            final JsonElement element = jsonArray.get(i);
            final Object object = handler.fromJson(type, element, type_args);
            if (object != null)
            {
                array[i] = object;
            }
            else
            {
                System.out.println("JsoNConverterArray: Error converting element to object using data[type=" + type + ", args=" + type_args + "] element=" + element);
                //TODO throw error if strict mode
            }
        }
        return array;
    }
}

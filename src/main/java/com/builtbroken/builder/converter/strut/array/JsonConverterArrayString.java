package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterArrayString extends JsonConverter
{

    public JsonConverterArrayString()
    {
        super("java:array.string");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object.getClass().isArray() && object.getClass().getComponentType().isPrimitive();
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        String[] array = (String[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (String s : array)
        {
            jsonArray.add(new JsonPrimitive(s));
        }
        return jsonArray;
    }

    @Override
    public Object fromJson(JsonElement input, String[] args)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        String[] array = new String[jsonArray.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsString();
        }
        return array;
    }
}

package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterArrayInt extends JsonConverter<Object>
{

    public JsonConverterArrayInt()
    {
        super("java:array.integer", "array.integer", "array.int", "int[]");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof int[];
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        int[] array = (int[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (int num : array)
        {
            jsonArray.add(new JsonPrimitive(num));
        }
        return jsonArray;
    }

    @Override
    public int[] fromJson(JsonElement input)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        int[] array = new int[jsonArray.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsInt();
        }
        return array;
    }
}

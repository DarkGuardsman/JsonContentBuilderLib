package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterArrayLong extends JsonConverter<Object>
{

    public JsonConverterArrayLong()
    {
        super("java:array.long", "array.long");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof long[];
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        long[] array = (long[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (long num : array)
        {
            jsonArray.add(new JsonPrimitive(num));
        }
        return jsonArray;
    }

    @Override
    public long[] fromJson(JsonElement input, String[] args)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        long[] array = new long[jsonArray.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsLong();
        }
        return array;
    }
}

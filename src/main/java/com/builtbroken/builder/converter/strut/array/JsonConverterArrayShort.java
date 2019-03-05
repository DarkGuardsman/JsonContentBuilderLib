package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterArrayShort extends JsonConverter<Object>
{

    public JsonConverterArrayShort()
    {
        super("java:array.short");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof short[];
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        short[] array = (short[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (short num : array)
        {
            jsonArray.add(new JsonPrimitive(num));
        }
        return jsonArray;
    }

    @Override
    public short[] fromJson(JsonElement input, String[] args)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        short[] array = new short[jsonArray.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsShort();
        }
        return array;
    }
}

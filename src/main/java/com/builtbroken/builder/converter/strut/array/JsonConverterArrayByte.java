package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterArrayByte extends JsonConverter<Object>
{

    public JsonConverterArrayByte()
    {
        super("java:array.byte", "array.byte");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof byte[];
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        byte[] array = (byte[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (byte num : array)
        {
            jsonArray.add(new JsonPrimitive(num));
        }
        return jsonArray;
    }

    @Override
    public byte[] fromJson(JsonElement input, String[] args)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        byte[] array = new byte[jsonArray.size()];
        for (byte i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsByte();
        }
        return array;
    }
}

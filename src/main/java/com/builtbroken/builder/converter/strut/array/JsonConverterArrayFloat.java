package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterArrayFloat extends JsonConverter<Object>
{

    public JsonConverterArrayFloat()
    {
        super("java:array.float", "array.float", "float[]");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof float[];
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        float[] array = (float[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (float num : array)
        {
            jsonArray.add(new JsonPrimitive(num));
        }
        return jsonArray;
    }

    @Override
    public float[] fromJson(JsonElement input)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        float[] array = new float[jsonArray.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsFloat();
        }
        return array;
    }
}

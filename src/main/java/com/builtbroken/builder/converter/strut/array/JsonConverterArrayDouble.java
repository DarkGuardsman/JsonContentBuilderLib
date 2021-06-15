package com.builtbroken.builder.converter.strut.array;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Robin Seifert on 2019-03-05.
 */
public class JsonConverterArrayDouble extends JsonConverter<Object>
{

    public JsonConverterArrayDouble()
    {
        super("java:array.double", "array.double", "double[]");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof double[];
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Object input)
    {
        double[] array = (double[]) input;

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (double num : array)
        {
            jsonArray.add(new JsonPrimitive(num));
        }
        return jsonArray;
    }

    @Override
    public double[] fromJson(JsonElement input)
    {
        final JsonArray jsonArray = input.getAsJsonArray();
        double[] array = new double[jsonArray.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = jsonArray.get(i).getAsDouble();
        }
        return array;
    }
}

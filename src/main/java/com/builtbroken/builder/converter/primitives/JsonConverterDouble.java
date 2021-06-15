package com.builtbroken.builder.converter.primitives;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Robin Seifert on 2019-03-05.
 */
public class JsonConverterDouble extends JsonConverterNumber
{
    public JsonConverterDouble()
    {
        super("java:double", "double", "d");
    }

    @Override
    public JsonElement toJson(Number object)
    {
        return new JsonPrimitive(object.doubleValue());
    }

    @Override
    public Double fromJson(JsonElement element)
    {
        return element.getAsJsonPrimitive().getAsDouble();
    }
}

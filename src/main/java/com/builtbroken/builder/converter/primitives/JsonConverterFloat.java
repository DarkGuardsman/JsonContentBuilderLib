package com.builtbroken.builder.converter.primitives;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterFloat extends JsonConverterNumber
{
    public JsonConverterFloat()
    {
        super("java:float", "float", "f");
    }

    @Override
    public JsonElement toJson(Number object)
    {
        return new JsonPrimitive(object.floatValue());
    }

    @Override
    public Float fromJson(JsonElement element)
    {
        return element.getAsJsonPrimitive().getAsFloat();
    }
}

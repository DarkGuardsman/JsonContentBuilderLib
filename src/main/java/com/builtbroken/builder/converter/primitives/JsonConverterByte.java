package com.builtbroken.builder.converter.primitives;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterByte extends JsonConverterNumber
{
    public JsonConverterByte()
    {
        super("java:byte", "byte", "b");
    }

    @Override
    public JsonElement toJson(Number object)
    {
        return new JsonPrimitive(object.floatValue());
    }

    @Override
    public Byte fromJson(JsonElement element)
    {
        return element.getAsJsonPrimitive().getAsByte();
    }
}

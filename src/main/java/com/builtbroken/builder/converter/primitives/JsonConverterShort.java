package com.builtbroken.builder.converter.primitives;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterShort extends JsonConverterNumber
{
    public JsonConverterShort()
    {
        super("java:short", "short", "s");
    }

    @Override
    public JsonElement toJson(Number object)
    {
        return new JsonPrimitive(object.shortValue());
    }

    @Override
    public Short fromJson(JsonElement element)
    {
        return element.getAsJsonPrimitive().getAsShort();
    }
}

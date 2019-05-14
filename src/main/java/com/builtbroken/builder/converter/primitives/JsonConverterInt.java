package com.builtbroken.builder.converter.primitives;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterInt extends JsonConverterNumber
{
    public JsonConverterInt()
    {
        super("java:integer", "int", "i");
    }

    @Override
    public JsonElement toJson(Number object)
    {
        return new JsonPrimitive(object.intValue());
    }

    @Override
    public Integer fromJson(JsonElement element)
    {
        return element.getAsJsonPrimitive().getAsInt();
    }
}

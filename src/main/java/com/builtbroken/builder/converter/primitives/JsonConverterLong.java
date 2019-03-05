package com.builtbroken.builder.converter.primitives;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterLong extends JsonConverterNumber
{
    public JsonConverterLong()
    {
        super("java:long");
    }

    @Override
    public String[] getAlias()
    {
        return new String[]{"long", "l"};
    }

    @Override
    public JsonElement toJson(Number object)
    {
        return new JsonPrimitive(object.longValue());
    }

    @Override
    public Long fromJson(JsonElement element)
    {
        return element.getAsJsonPrimitive().getAsLong();
    }
}

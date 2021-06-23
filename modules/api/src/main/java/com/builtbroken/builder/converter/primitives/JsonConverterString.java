package com.builtbroken.builder.converter.primitives;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Robin Seifert on 2019-03-05.
 */
public class JsonConverterString extends JsonConverter<String>
{

    public JsonConverterString()
    {
        super("java:string", "string");
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof String;
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        if (json instanceof JsonPrimitive)
        {
            return json.getAsJsonPrimitive().isString();
        }
        return false;
    }

    @Override
    public JsonElement toJson(String object)
    {
        return new JsonPrimitive(object);
    }

    @Override
    public String fromJson(JsonElement element)
    {
        return element.getAsString();
    }
}

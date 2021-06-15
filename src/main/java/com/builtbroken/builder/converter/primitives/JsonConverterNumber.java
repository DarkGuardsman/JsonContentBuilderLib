package com.builtbroken.builder.converter.primitives;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Robin Seifert on 2019-03-05.
 */
public abstract class JsonConverterNumber extends JsonConverter<Number>
{
    public JsonConverterNumber(String id, String... alts)
    {
        super(id, alts);
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof Number;
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        if (json instanceof JsonPrimitive)
        {
            return json.getAsJsonPrimitive().isNumber();
        }
        return false;
    }
}

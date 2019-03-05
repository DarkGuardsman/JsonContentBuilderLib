package com.builtbroken.builder.converter.primitives;

import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class JsonConverterInt extends JsonConverterNumber
{
    public JsonConverterInt()
    {
        super("java:integer");
    }

    @Override
    public String[] getAlias()
    {
        return new String[]{"int", "i"};
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

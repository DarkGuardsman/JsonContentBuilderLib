package com.builtbroken.builder.converter;

import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public abstract class JsonConverter<O extends Object> implements IJsonConverter<O>
{

    private final String id;
    private final String[] alts;

    public JsonConverter(String id, String... alts)
    {
        this.id = id;
        this.alts = alts;
    }

    @Override
    public String[] getAlias()
    {
        return alts;
    }

    @Override
    public String getUniqueID()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "JsonConverter[" + getUniqueID() + "]";
    }

    @Override
    public JsonElement toJson(O object, String[] args)
    {
        return toJson(object);
    }

    @Override
    public O fromJson(JsonElement element, String[] args)
    {
        return fromJson(element);
    }
}

package com.builtbroken.builder.converter;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public abstract class JsonConverter<O extends Object> implements IJsonConverter<O>
{
    private final String id;

    public JsonConverter(String id)
    {
        this.id = id;
    }

    @Override
    public String getUniqueID()
    {
        return id;
    }
}

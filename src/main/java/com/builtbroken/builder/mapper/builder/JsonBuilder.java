package com.builtbroken.builder.mapper.builder;

/**
 * Created by Robin Seifert on 2019-05-14.
 */
public abstract class JsonBuilder implements IJsonBuilder
{
    protected final String type;

    protected JsonBuilder(String type)
    {
        this.type = type;
    }

    @Override
    public String getUniqueID()
    {
        return type;
    }
}

package com.builtbroken.builder.mapper.linker;

import com.builtbroken.builder.mapper.mappers.IJsonMapper;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public abstract class JsonLinker<O extends Object> implements IJsonLinker<O>
{

    private final String[] names;
    protected final String type;
    protected final boolean required;

    public JsonLinker(String[] names, String type, boolean required)
    {
        this.names = names;
        this.type = type;
        this.required = required;
    }

    @Override
    public boolean isValid(O object)
    {
        return required;
    }

    @Override
    public String[] getKeys()
    {
        return names;
    }

    @Override
    public String getType()
    {
        return type;
    }
}

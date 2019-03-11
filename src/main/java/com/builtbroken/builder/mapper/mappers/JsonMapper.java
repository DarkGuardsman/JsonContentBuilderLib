package com.builtbroken.builder.mapper.mappers;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public abstract class JsonMapper<O extends Object> implements IJsonMapper<O>
{

    private final String[] names;
    protected final String type;
    protected final String[] args;

    public JsonMapper(String[] names, String type, String[] args)
    {
        this.names = names;
        this.type = type;
        this.args = args;
    }

    @Override
    public boolean isValid(O object)
    {
        return true;
    }

    @Override
    public String[] getKeys()
    {
        return names;
    }
}

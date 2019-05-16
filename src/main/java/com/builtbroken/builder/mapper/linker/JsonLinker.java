package com.builtbroken.builder.mapper.linker;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public abstract class JsonLinker<O extends Object> implements IJsonLinker<O>
{

    private final String[] names;
    protected final String type;
    protected final boolean required;
    protected final String prefix;

    public JsonLinker(String[] names, String type, String prefix, boolean required)
    {
        this.names = names;
        this.type = type;
        this.required = required;
        this.prefix = prefix;
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

    @Override
    public void destroy()
    {

    }
}

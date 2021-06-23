package com.builtbroken.builder.converter.strut;

import com.builtbroken.builder.converter.JsonConverter;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.google.gson.JsonElement;

import java.util.function.Function;

/**
 * Created by Robin Seifert on 2019-04-05.
 */
public class ConverterObjectBuilder<C extends IJsonGeneratedObject> extends JsonConverter<C>
{

    public final Function<JsonElement, C> factory;
    public final Class<C> clazz;

    public ConverterObjectBuilder(String id, Class<C> clazz, Function<JsonElement, C> factory)
    {
        super(id);
        this.factory = factory;
        this.clazz = clazz;
    }

    @Override
    public C fromJson(JsonElement element)
    {
        return factory.apply(element);
    }

    @Override
    public boolean canSupport(Object object)
    {
        return false; //TODO find a way to we can convert to JSON easily
        //return object.getClass() == clazz;
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return true; //TODO think of a way to validate?
    }
}

package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Created by Robin Seifert on 2019-05-14.
 */
public class JsonBuilderFunction extends JsonBuilder
{
    private final Function supplier;

    public JsonBuilderFunction(String type, Function function)
    {
        super(type);
        this.supplier = function;
    }

    @Override
    public Object newObject(@Nonnull JsonElement data, @Nonnull ConversionHandler converter)
    {
        return supplier.apply(data);
    }
}

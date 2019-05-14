package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class JsonBuilderSupplier extends JsonBuilder
{
    private final Supplier supplier;

    public JsonBuilderSupplier(String type, Supplier supplier)
    {
        super(type);
        this.supplier = supplier;
    }

    @Override
    public Object newObject(@Nonnull JsonElement data, @Nonnull ConversionHandler converter)
    {
        return supplier.get();
    }
}

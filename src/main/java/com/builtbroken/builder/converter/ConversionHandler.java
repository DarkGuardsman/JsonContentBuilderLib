package com.builtbroken.builder.converter;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class ConversionHandler
{
    private final Map<String, IJsonConverter> converters = new HashMap();
    private final Map<String, IJsonConverter> altNames = new HashMap();

    private final ConversionHandler parent; //TODO check for cycle dependency
    private final String name;

    public ConversionHandler(ConversionHandler parent, String name)
    {
        this.parent = parent;
        this.name = name;
    }

    public Object fromJson(String type, JsonElement element)
    {
        type = type.toLowerCase();
        if (converters.containsKey(type))
        {
            final IJsonConverter converter = converters.get(type);
            if (converter != null && converter.canSupport(element))
            {
                return converter.fromJson(element);
            }
        }
        else if (altNames.containsKey(type))
        {
            final IJsonConverter converter = altNames.get(type);
            if (converter != null && converter.canSupport(element))
            {
                return converter.fromJson(element);
            }
        }
        else if (parent != null)
        {
            return parent.fromJson(type, element);
        }
        return null; //TODO throw error
    }

    public JsonElement toJson(String type, Object object)
    {
        type = type.toLowerCase();
        if (converters.containsKey(type))
        {
            final IJsonConverter converter = converters.get(type);
            if (converter != null && converter.canSupport(object))
            {
                return converter.toJson(object);
            }
        }
        else if (altNames.containsKey(type))
        {
            final IJsonConverter converter = altNames.get(type);
            if (converter != null && converter.canSupport(object))
            {
                return converter.toJson(object);
            }
        }
        else if (parent != null)
        {
            return parent.toJson(type, object);
        }
        return null; //TODO throw error
    }
}

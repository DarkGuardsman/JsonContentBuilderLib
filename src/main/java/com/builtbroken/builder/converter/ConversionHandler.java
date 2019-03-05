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

    public Object fromJson(String type, JsonElement element, String[] args)
    {
        type = type.toLowerCase();
        if (converters.containsKey(type))
        {
            final IJsonConverter converter = converters.get(type);
            if (converter != null && converter.canSupport(element))
            {
                return converter.fromJson(element, args);
            }
        }
        else if (altNames.containsKey(type))
        {
            final IJsonConverter converter = altNames.get(type);
            if (converter != null && converter.canSupport(element))
            {
                return converter.fromJson(element, args);
            }
        }
        else if (parent != null)
        {
            return parent.fromJson(type, element, args);
        }
        return null; //TODO throw error
    }

    public JsonElement toJson(String type, Object object, String[] args)
    {
        type = type.toLowerCase();
        if (converters.containsKey(type))
        {
            final IJsonConverter converter = converters.get(type);
            if (converter != null && converter.canSupport(object))
            {
                return converter.toJson(object, args);
            }
        }
        else if (altNames.containsKey(type))
        {
            final IJsonConverter converter = altNames.get(type);
            if (converter != null && converter.canSupport(object))
            {
                return converter.toJson(object, args);
            }
        }
        else if (parent != null)
        {
            return parent.toJson(type, object, args);
        }
        return null; //TODO throw error
    }

    public void addConverter(IJsonConverter converter)
    {
        if (converter != null)
        {
            if (converter.getUniqueID() != null && !converter.getUniqueID().trim().isEmpty())
            {
                final String type = converter.getUniqueID().toLowerCase();
                if (converters.containsKey(type))
                {
                    System.out.println(this + ": Warning, overriding existing converter " +
                            "[" + converters.get(type) + "] " +
                            "with [" + converter + "] for id " + type);
                    //TODO throw error if strict mode, and use logger
                }
                converters.put(type, converter);
                converter.onRegistered(this);


                if (converter.getAlias() != null)
                {
                    for (String string : converter.getAlias())
                    {
                        if (string != null && !string.trim().isEmpty())
                        {
                            if (altNames.containsKey(string.toLowerCase()))
                            {
                                throw new IllegalArgumentException(this + ": alias[" + string + "] is already in use by another converter " +
                                        "Current: " + altNames.get(string.toLowerCase())
                                        + " New: " + converter);
                            }
                            altNames.put(string.toLowerCase(), converter);
                        }
                        else
                        {
                            throw new IllegalArgumentException(this + ": invalid alias[" + string + "] for converter " + converter);
                        }
                    }
                }
            }
            else
            {
                throw new IllegalArgumentException(this + ": converters require unique IDs to be set");
            }
        }
        else
        {
            throw new IllegalArgumentException(this + ": Can not add a null converter");
        }
    }

    public IJsonConverter getConverter(String type)
    {
        type = type.toLowerCase();
        if (converters.containsKey(type))
        {
            final IJsonConverter converter = converters.get(type);
            if (converter != null)
            {
                return converter;
            }
        }

        if (altNames.containsKey(type))
        {
            final IJsonConverter converter = altNames.get(type);
            if (converter != null)
            {
                return converter;
            }
        }

        if (parent != null)
        {
            return parent.getConverter(type);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "ConversionHandler[" + name + "]@" + hashCode();
    }
}

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

    public ConversionHandler addConverter(IJsonConverter converter)
    {
        if (converter != null)
        {
            final String type = formatKey(converter.getUniqueID());
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
                        string = formatKey(string);
                        if (altNames.containsKey(string))
                        {
                            throw new IllegalArgumentException(this + ": alias[" + string + "] is already in use by another converter " +
                                    "Current: " + altNames.get(string)
                                    + " New: " + converter);
                        }
                        altNames.put(string, converter);
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
            throw new IllegalArgumentException(this + ": Can not add a null converter");
        }
        return this;
    }

    private String formatKey(String key)
    {
        if (key == null)
        {
            throw new RuntimeException(this + ": converters require unique IDs to be set");
        }
        if (key.contains(";"))
        {
            throw new RuntimeException(this + ": Converter name [" + key + "] can not contain ; ");
        }
        key = key.trim().toLowerCase();
        if (key.isEmpty())
        {
            throw new RuntimeException(this + ": Converter name [" + key + "] can not be an empty string");
        }

        return key;
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

    public void destroy()
    {
        converters.clear();
        altNames.clear();
    }

    @Override
    public String toString()
    {
        return "ConversionHandler[" + name + "]@" + hashCode();
    }
}

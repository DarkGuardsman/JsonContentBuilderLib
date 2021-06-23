package com.builtbroken.builder.converter.strut;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.converter.JsonConverter;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Function;

/**
 * simple converter wrapper that allows for nested JSON objects to easily be used for value assignment
 * Created by Robin Seifert on 2019-6-20.
 */
public class JsonConverterObject extends JsonConverter<Object>
{

    private ConversionHandler handler;

    public JsonConverterObject()
    {
        super("java:object", "object", "obj");
    }

    @Override
    public void onRegistered(ConversionHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public Object fromJson(JsonElement element)
    {
        if (element.isJsonObject())
        {
            final JsonObject json = element.getAsJsonObject();

            final String type = json.getAsJsonPrimitive("type").getAsString();
            final String[] args = getArgs(json);
            return handler.fromJson(type, json, args);
        }
        else
        {
            throw new IllegalArgumentException("Json Object converter requires an object as input");
        }
    }

    @Override
    public boolean canSupport(Object object)
    {
        return false; //TODO add a way to handle
    }

    private String[] getArgs(JsonObject json)
    {
        if (json.has("args"))
        {
            return (String[]) handler.fromJson(ConverterRefs.ARRAY, json.get("args"), new String[]{ConverterRefs.STRING});
        }
        return null;
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return true; //TODO think of a way to validate?
    }
}

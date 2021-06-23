package com.builtbroken.builder.data;

import com.google.gson.JsonObject;

/**
 * Used to temp store generated objects with metadata
 * Created by Robin Seifert on 2019-03-05.
 */
public class GeneratedObject
{
    public final String type;
    public final Object objectCreated;
    public final JsonObject jsonUsed;

    public GeneratedObject(String type, Object objectCreated, JsonObject jsonUsed)
    {
        this.type = type;
        this.objectCreated = objectCreated;
        this.jsonUsed = jsonUsed;
    }
}

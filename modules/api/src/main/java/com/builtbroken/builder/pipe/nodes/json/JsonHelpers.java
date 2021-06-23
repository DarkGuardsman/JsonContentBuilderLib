package com.builtbroken.builder.pipe.nodes.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Robin Seifert on 6/18/2021.
 */
public class JsonHelpers
{
    public static Gson gson = new Gson();

    private static Method jsonObjectCopy;
    private static Method jsonArrayCopy;

    public static JsonObject deepCopy(JsonObject object)
    {
        if(jsonObjectCopy == null) {
            try
            {
                jsonObjectCopy = JsonObject.class.getDeclaredMethod("deepCopy");
                jsonObjectCopy.setAccessible(true);
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException("Failed to reflect JsonObject#deepCopy method");
            }
        }
        try
        {
            return (JsonObject) jsonObjectCopy.invoke(object);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonArray deepCopy(JsonArray object)
    {
        if(jsonArrayCopy == null) {
            try
            {
                jsonArrayCopy = JsonArray.class.getDeclaredMethod("deepCopy");
                jsonArrayCopy.setAccessible(true);
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException("Failed to reflect JsonObject#deepCopy method");
            }
        }
        try
        {
            return (JsonArray) jsonArrayCopy.invoke(object);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

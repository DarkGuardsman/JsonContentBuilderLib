package com.builtbroken.builder.mapper;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.mapper.mappers.JsonClassMapper;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Handles mapping JSON data to fields/methods
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonMappingHandler
{

    //Class to mapper, not all mappers will have a matching key
    private static final HashMap<Class, JsonClassMapper> clazzMappers = new HashMap();
    //Key to class, a single class can have several keys
    private static final HashMap<String, Class> keyToClass = new HashMap();

    public static void map(String key, Object object, JsonObject json, ConversionHandler handler)
    {
        key = key.toLowerCase();
        if (keyToClass.containsKey(key))
        {
            Class clazz = keyToClass.get(key);
            if (clazzMappers.containsKey(clazz))
            {
                JsonClassMapper mapper = clazzMappers.get(clazz);
                if (mapper != null)
                {
                    try
                    {
                        mapper.map(json, object, handler);
                    } catch (Exception e)
                    {
                        throw new RuntimeException("JsonMappingHandler: Failed to map data to object. "
                                + "\n Key: " + key
                                + "\n Class: " + clazz
                                + "\n Object: " + object
                                + "\n Json: " + json
                                , e);
                    }
                }
                else
                {
                    //TODO error
                }
            }
            else
            {
                //TODO error
            }
        }
        else
        {
            //TODO error
        }
    }


    /**
     * Called to register a class for mapping
     *
     * @param clazz
     * @param keys
     */
    public static void register(Class clazz, String... keys)
    {
        //Store keys
        for (String string : keys)
        {
            keyToClass.put(string.toLowerCase(), clazz);
        }

        //Map classes, do separate from keys as we might have a parent class registered out of order
        if (!clazzMappers.containsKey(clazz))
        {
            //Create
            JsonClassMapper mapper = new JsonClassMapper(clazz).init();
            clazzMappers.put(clazz, mapper);

            //Map parent classes
            while (clazz.getSuperclass() != Object.class && clazzMappers.containsKey(clazz.getSuperclass()))
            {
                //Parent class
                clazz = clazz.getSuperclass();

                //Create parent mapper
                JsonClassMapper mapper2 = new JsonClassMapper(clazz).init();
                clazzMappers.put(clazz, mapper);

                //Set previous mapper's parent
                mapper.setParent(mapper2);

                //Set current as last so we can set parent
                mapper = mapper2;
            }
        }
    }
}

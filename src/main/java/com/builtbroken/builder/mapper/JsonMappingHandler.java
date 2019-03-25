package com.builtbroken.builder.mapper;

import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.mapper.mappers.IJsonMapper;
import com.builtbroken.builder.mapper.mappers.JsonClassMapper;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Handles mapping JSON data to fields/methods
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonMappingHandler //TODO consider making per builder instance
{

    //Class to mapper, not all mappers will have a matching key
    private static final HashMap<Class, JsonClassMapper> clazzMappers = new HashMap();
    //Key to class, a single class can have several keys
    private static final HashMap<String, Class> keyToClass = new HashMap();

    public static void map(String objectType, Object objectToMap, JsonObject jsonToUse, ContentLoader loader, boolean links)
    {
        final JsonClassMapper mapper = getMapper(objectType);
        try
        {
            if (!links)
            {
                mapper.mapDataFields(jsonToUse, objectToMap, loader.conversionHandler);
            }
            else
            {
                mapper.mapDataLinks(jsonToUse, objectToMap, loader.jsonObjectHandlerRegistry);
            }
        } catch (Exception e)
        {
            throw new RuntimeException("JsonMappingHandler: Failed to map " + (links ? "links" : "data") + " to object. "
                    + "\n Key: " + objectType
                    + "\n Class: " + mapper.clazz
                    + "\n Object: " + objectToMap
                    + "\n Json: " + jsonToUse
                    , e);
        }
    }

    public static void validate(String objectType, Object objectToMap)
    {
        getMapper(objectType).validate(objectToMap);
    }

    private static JsonClassMapper getMapper(String objectType)
    {
        objectType = objectType.toLowerCase();
        if (keyToClass.containsKey(objectType))
        {
            Class clazz = keyToClass.get(objectType);
            if (clazzMappers.containsKey(clazz))
            {
                JsonClassMapper mapper = clazzMappers.get(clazz);
                if (mapper != null)
                {
                    return mapper;
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


        return null;
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

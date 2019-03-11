package com.builtbroken.builder.mapper;

import com.builtbroken.builder.mapper.mappers.JsonClassMapper;

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

    /**
     * Called to register a class for mapping
     *
     * @param clazz
     * @param keys
     */
    public static void register(Class clazz, String[] keys)
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

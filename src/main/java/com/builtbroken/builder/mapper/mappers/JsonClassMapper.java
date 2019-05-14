package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.builtbroken.builder.mapper.linker.IJsonLinker;
import com.builtbroken.builder.mapper.linker.JsonFieldLinker;
import com.builtbroken.builder.mapper.linker.JsonMethodLinker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonClassMapper
{

    private final HashMap<String, IJsonMapper> mappings = new HashMap();
    private final HashMap<String, IJsonLinker> linkMappers = new HashMap();

    private JsonClassMapper parent;
    public final Class clazz;

    public JsonClassMapper(Class clazz)
    {
        this.clazz = clazz;
    }

    public JsonClassMapper init()
    {
        //Handle fields
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            //Field can have either a mapping or a wire, but not both.
            //      However, both can share keys as they can't overlap.
            //      Ex: data field to store the ID
            //          wire to store the reference of the ID
            JsonMapping mapping = field.getAnnotation(JsonMapping.class);
            if (mapping != null)
            {
                JsonFieldMapper mapper = new JsonFieldMapper(clazz, field, mapping);
                for (String key : mapping.keys())
                {
                    mappings.put(key.toLowerCase(), mapper);
                }
            }
            else
            {
                JsonObjectWiring objectWiring = field.getAnnotation(JsonObjectWiring.class);
                if (objectWiring != null)
                {
                    JsonFieldLinker linker = new JsonFieldLinker(field, objectWiring);
                    for (String key : linker.getKeys())
                    {
                        linkMappers.put(key.toLowerCase(), linker);
                    }
                }
            }
        }

        //Handle methods
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods)
        {
            JsonMapping mapping = method.getAnnotation(JsonMapping.class);
            if (mapping != null)
            {
                JsonMethodMapper mapper = new JsonMethodMapper(method, mapping);
                for (String key : mapping.keys())
                {
                    mappings.put(key.toLowerCase(), mapper);
                }
            }
            else
            {
                JsonObjectWiring objectWiring = method.getAnnotation(JsonObjectWiring.class);
                if (objectWiring != null)
                {
                    JsonMethodLinker linker = new JsonMethodLinker(method, objectWiring);
                    for (String key : linker.getKeys())
                    {
                        linkMappers.put(key.toLowerCase(), linker);
                    }
                }
            }
        }
        return this;
    }

    public void mapDataFields(JsonObject json, Object objectToMap, ConversionHandler handler)
    {
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            final String key = entry.getKey().toLowerCase();
            final JsonElement data = entry.getValue();
            if (mappings.containsKey(key))
            {
                mappings.get(key).map(objectToMap, data, handler);
            }
            else
            {
                //TODO check for flattening of JSON up a level
                //        Ex: data/tree -> tree
                //          Ignore links
            }
        }
        if (getParent() != null)
        {
            getParent().mapDataFields(json, objectToMap, handler);
        }
    }

    public void mapDataLinks(JsonObject json, Object object, JsonObjectHandlerRegistry registry)
    {
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            final String key = entry.getKey().toLowerCase();
            final JsonElement data = entry.getValue();
            if (linkMappers.containsKey(key))
            {
                linkMappers.get(key).link(object, data, registry);
            }
            else
            {
                //TODO check for flattening of JSON up a level
                //        Ex: data/tree -> tree
                //          Ignore links
            }
        }
        if (getParent() != null)
        {
            getParent().mapDataLinks(json, object, registry);
        }
    }

    public void validate(Object object)
    {
        for (IJsonMapper mapper : mappings.values())
        {
            mapper.isValid(object);
        }
        for (IJsonLinker mapper : linkMappers.values())
        {
            mapper.isValid(object);
        }

        if (getParent() != null)
        {
            getParent().validate(object);
        }
    }

    public JsonClassMapper getParent()
    {
        return parent;
    }

    public void setParent(JsonClassMapper parent)
    {
        this.parent = parent;
    }

    public void destroy()
    {
        mappings.values().forEach(mappers -> mappers.destroy());
        mappings.clear();

        linkMappers.values().forEach(mappers -> mappers.destroy());
        linkMappers.clear();
    }
}

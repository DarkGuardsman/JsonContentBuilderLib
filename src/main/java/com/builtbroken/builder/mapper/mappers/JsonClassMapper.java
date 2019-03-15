package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.JsonMapping;
import com.builtbroken.builder.mapper.JsonObjectWiring;
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
    private final HashMap<String, IJsonMapper> linkMappers = new HashMap();

    private JsonClassMapper parent;
    private final Class clazz;

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
            JsonMapping mapping = field.getAnnotation(JsonMapping.class);
            if (mapping != null)
            {
                JsonFieldMapper mapper = new JsonFieldMapper(field, mapping);
                for (String key : mapping.keys())
                {
                    mappings.put(key.toLowerCase(), mapper);
                }
            }
            else
            {
                JsonObjectWiring objectWiring = field.getAnnotation(JsonObjectWiring.class);
                if(objectWiring != null)
                {
                    JsonFieldMapper mapper = new JsonFieldMapper(field, mapping);
                    for (String key : mapping.keys())
                    {
                        mappings.put(key.toLowerCase(), mapper);
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
        }
        return this;
    }

    public void mapDataFields(JsonObject json, Object object, ConversionHandler handler)
    {
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            final String key = entry.getKey().toLowerCase();
            final JsonElement data = entry.getValue();
            if (mappings.containsKey(key))
            {
                mappings.get(key).map(object, data, handler);
            }
            else
            {
                //TODO check for flattening of JSON up a level
                //        Ex: data/tree -> tree
            }
        }
        if (getParent() != null)
        {
            getParent().mapDataFields(json, object, handler);
        }
    }

    public void mapDataLinks(JsonObject json, Object object, JsonObjectHandlerRegistry registry)
    {

    }

    public JsonClassMapper getParent()
    {
        return parent;
    }

    public void setParent(JsonClassMapper parent)
    {
        this.parent = parent;
    }
}

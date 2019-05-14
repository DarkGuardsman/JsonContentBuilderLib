package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.builtbroken.builder.mapper.builder.IJsonBuilder;
import com.builtbroken.builder.mapper.builder.JsonBuilder;
import com.builtbroken.builder.mapper.builder.JsonBuilderConstructor;
import com.builtbroken.builder.mapper.builder.JsonBuilderMethod;
import com.builtbroken.builder.mapper.linker.IJsonLinker;
import com.builtbroken.builder.mapper.linker.JsonFieldLinker;
import com.builtbroken.builder.mapper.linker.JsonMethodLinker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonClassMapper
{

    private final HashMap<String, IJsonMapper> mappings = new HashMap();
    private final HashMap<String, IJsonLinker> linkMappers = new HashMap();
    private final HashMap<String, IJsonBuilder> jsonBuilders = new HashMap();

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
            final JsonMapping mapping = field.getAnnotation(JsonMapping.class);
            if (mapping != null)
            {
                if (Modifier.isStatic(field.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Mapping can not be applied to a static field. "
                            + " Class: " + clazz
                            + " Field: " + field.toString());
                }

                JsonFieldMapper mapper = new JsonFieldMapper(clazz, field, mapping);
                for (String key : mapping.keys())
                {
                    mappings.put(key.toLowerCase(), mapper);
                }
            }

            final JsonObjectWiring objectWiring = field.getAnnotation(JsonObjectWiring.class);
            if (objectWiring != null)
            {
                if(mapping != null)
                {
                    throw new RuntimeException("JsonClassMapper: A field can be a mapping injection point"
                            + " or an auto wire injection point, not both. "
                            + " Class: " + clazz
                            + " Field: " + field.toString());
                }
                else if (Modifier.isStatic(field.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Auto wiring can not be applied to a static field. "
                            + " Class: " + clazz
                            + " Field: " + field.toString());
                }

                JsonFieldLinker linker = new JsonFieldLinker(field, objectWiring);
                for (String key : linker.getKeys())
                {
                    linkMappers.put(key.toLowerCase(), linker);
                }
            }
        }

        //Handle methods
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods)
        {
            //Mapping handling
            final JsonMapping mapping = method.getAnnotation(JsonMapping.class);
            if (mapping != null)
            {
                //Error
                if (Modifier.isStatic(method.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Mapping can not be applied to a static method. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }

                JsonMethodMapper mapper = new JsonMethodMapper(method, mapping);
                for (String key : mapping.keys())
                {
                    mappings.put(key.toLowerCase(), mapper);
                }
            }

            //Wiring handling
            final JsonObjectWiring objectWiring = method.getAnnotation(JsonObjectWiring.class);
            if (objectWiring != null)
            {
                //Error
                if (mapping != null)
                {
                    throw new RuntimeException("JsonClassMapper: A method can be a mapping injection point"
                            + " or an auto wire injection point, not both. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }
                else if (Modifier.isStatic(method.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Autowiring can not be applied to a static method. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }

                JsonMethodLinker linker = new JsonMethodLinker(method, objectWiring);
                for (String key : linker.getKeys())
                {
                    linkMappers.put(key.toLowerCase(), linker);
                }
            }

            //Construction handling
            final JsonConstructor jsonConstructor = method.getAnnotation(JsonConstructor.class);
            if (jsonConstructor != null)
            {
                //Error
                if (!Modifier.isStatic(method.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Can't apply an object constructor to a non-static method. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }

                final String key = jsonConstructor.type().toLowerCase();

                if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(JsonElement.class))
                {
                    //Json only constructor
                    jsonBuilders.put(key, new JsonBuilderMethod(clazz, key, method, null, false));
                }
                else if (method.getParameterCount() > 0)
                {
                    //Locate all mappings
                    final JsonMapping[] mappers = new JsonMapping[method.getParameterCount()];
                    final Annotation[][] paraAnnos = method.getParameterAnnotations();
                    for (int para = 0; para < mappers.length; para++)
                    {
                        for (Annotation annotation : paraAnnos[para])
                        {
                            if (annotation instanceof JsonMapping)
                            {
                                mappers[para] = (JsonMapping) annotation;
                                break;
                            }
                        }

                        if (mappers[para] == null)
                        {
                            new RuntimeException("JsonClassMapper: All method parameters require JsonMapping annotation "
                                    + "when used with JsonConstructor annotation."
                                    + " Class: " + clazz
                                    + " Method: " + method);
                        }
                    }

                    //Mapper constructor
                    jsonBuilders.put(key, new JsonBuilderMethod(clazz, key, method, mappers, jsonConstructor.useConstructorData()));
                }
                else
                {
                    //Default constructor
                    jsonBuilders.put(key, new JsonBuilderMethod(clazz, key, method, null, false));
                }

            }
        }

        //Handle Constructors
        final Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors)
        {
            final JsonConstructor jsonConstructor = (JsonConstructor) constructor.getAnnotation(JsonConstructor.class);
            if(jsonConstructor != null)
            {
                final String key = jsonConstructor.type().toLowerCase();

                if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].isAssignableFrom(JsonElement.class))
                {
                    //Json only constructor
                    jsonBuilders.put(key, new JsonBuilderConstructor(clazz, key, constructor, null, false));
                }
                else if (constructor.getParameterCount() > 0)
                {
                    //Locate all mappings
                    final JsonMapping[] mappers = new JsonMapping[constructor.getParameterCount()];
                    final Annotation[][] paraAnnos = constructor.getParameterAnnotations();
                    for (int para = 0; para < mappers.length; para++)
                    {
                        for (Annotation annotation : paraAnnos[para])
                        {
                            if (annotation instanceof JsonMapping)
                            {
                                mappers[para] = (JsonMapping) annotation;
                                break;
                            }
                        }

                        if (mappers[para] == null)
                        {
                            new RuntimeException("JsonClassMapper: All constructor parameters require JsonMapping annotation "
                                    + "when used with JsonConstructor annotation."
                                    + " Class: " + clazz
                                    + " Constructor: " + constructor);
                        }
                    }

                    //Mapper constructor
                    jsonBuilders.put(key, new JsonBuilderConstructor(clazz, key, constructor, mappers, jsonConstructor.useConstructorData()));
                }
                else
                {
                    //Default constructor
                    jsonBuilders.put(key, new JsonBuilderConstructor(clazz, key, constructor, null, false));
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

    public IJsonBuilder getBuilder(String type)
    {
        return jsonBuilders.get(type.toLowerCase());
    }

    public void destroy()
    {
        mappings.values().forEach(mappers -> mappers.destroy());
        mappings.clear();

        linkMappers.values().forEach(mappers -> mappers.destroy());
        linkMappers.clear();
    }
}

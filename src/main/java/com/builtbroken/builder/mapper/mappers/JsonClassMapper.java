package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.MapperHelpers;
import com.builtbroken.builder.mapper.anno.*;
import com.builtbroken.builder.mapper.builder.*;
import com.builtbroken.builder.mapper.injection.IJsonInjectionMapper;
import com.builtbroken.builder.mapper.injection.JsonInjectionMapper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonClassMapper
{

    private final HashMap<String, IJsonMapper> mappings = new HashMap(); //TODO allow more than 1 mapper
    private final HashMap<String, List<IJsonLinker>> linkMappers = new HashMap();
    private final HashMap<String, IJsonBuilder> jsonBuilders = new HashMap(); //TODO allow more than 1 constructor matching best
    private final List<IJsonInjectionMapper> injectionMappers = new ArrayList();

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

                final JsonFieldMapper mapper = new JsonFieldMapper(clazz, field, mapping);
                for (String key : mapping.keys())
                {
                    mappings.put(key.toLowerCase(), mapper);
                    System.out.println("JsonClassMapper[" + clazz + "] MAP: " + key + " -> " + field);
                }
            }

            final JsonObjectWiring objectWiring = field.getAnnotation(JsonObjectWiring.class);
            if (objectWiring != null)
            {
                if (mapping != null)
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

                final JsonFieldLinker linker = new JsonFieldLinker(field, objectWiring);
                for (String key : linker.getKeys())
                {
                    addLink(key, linker);
                    System.out.println("JsonClassMapper[" + clazz + "] LINK: " + key + " -> " + field);
                }
            }

            final JsonConstructor jsonConstructor = field.getAnnotation(JsonConstructor.class);
            if (jsonConstructor != null)
            {
                if (!Modifier.isStatic(field.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Can't apply an object constructor to a non-static field. "
                            + " Class: " + clazz
                            + " Field: " + field.toString());
                }

                final String key = jsonConstructor.type().toLowerCase();

                if (field.getType().isAssignableFrom(Supplier.class))
                {
                    System.out.println("JsonClassMapper[" + clazz + "] SUPPLY: " + key + " -> " + field);
                    //Could have used a default constructor but ok
                    jsonBuilders.put(key, new JsonBuilderSupplier(key, () ->
                    {
                        try
                        {
                            return ((Supplier) field.get(null)).get();
                        } catch (Exception e)
                        {
                            throw new RuntimeException("JsonClassMapper: Unexpected error invoking supplier to create new object", e);
                        }
                    }));
                }
                else if (field.getType().isAssignableFrom(Function.class))
                {
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE: " + key + " -> " + field);
                    //Could have used a default constructor but ok
                    jsonBuilders.put(key, new JsonBuilderFunction(key, (json) ->
                    {
                        try
                        {
                            return ((Function) field.get(null)).apply(json);
                        } catch (Exception e)
                        {
                            throw new RuntimeException("JsonClassMapper: Unexpected error invoking lambda function to create new object", e);
                        }
                    }));
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
                else if (method.getParameterCount() != 1)
                {
                    throw new RuntimeException("JsonClassMapper: Mapping only supports a single parameter for input. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }

                final JsonMethodMapper mapper = new JsonMethodMapper(clazz, method, mapping);
                for (String key : mapping.keys())
                {
                    System.out.println("JsonClassMapper[" + clazz + "] MAP: " + key + " -> " + method);
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
                    throw new RuntimeException("JsonClassMapper: Field wiring annotation is not compatible"
                            + " with mapping annotation."
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }
                else if (Modifier.isStatic(method.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Autowiring can not be applied to a static method. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }
                else if (method.getParameterCount() != 1)
                {
                    throw new RuntimeException("JsonClassMapper: Autowiring only supports a single parameter for input. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }

                final JsonMethodLinker linker = new JsonMethodLinker(method, objectWiring);
                for (String key : linker.getKeys())
                {
                    System.out.println("JsonClassMapper[" + clazz + "] LINK: " + key + " -> " + method);
                    addLink(key, linker);
                }
            }

            //Injection handling
            final JsonInjection injection = method.getAnnotation(JsonInjection.class);
            if (injection != null)
            {
                if (mapping != null || objectWiring != null)
                {
                    throw new RuntimeException("JsonClassMapper: Injection mapping annotation is not compatible"
                            + " with mapping or wiring annotation."
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }
                else if (Modifier.isStatic(method.getModifiers()))
                {
                    throw new RuntimeException("JsonClassMapper: Injection mapping can not be applied to a static method. "
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }
                else if (method.getParameterCount() == 0)
                {
                    throw new RuntimeException("JsonClassMapper: JsonInjection requires at least 1 parameter."
                            + " Class: " + clazz
                            + " Method: " + method.toString());
                }

                //Locate all mappings
                final BiFunction<JsonObject, ConversionHandler, Object>[] mappers
                        = MapperHelpers.buildMappers(method.getParameterTypes(),
                        method.getParameterAnnotations(),
                        () -> " Class: " + clazz + " Method: " + method.toString());

                //Create
                injectionMappers.add(new JsonInjectionMapper(method, mappers));
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
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE_J: " + key + " -> " + method);
                    jsonBuilders.put(key, new JsonBuilderMethod(clazz, key, method, null, false));
                }
                else if (method.getParameterCount() > 0)
                {
                    //Locate all mappings
                    final BiFunction<JsonObject, ConversionHandler, Object>[] mappers = new BiFunction[method.getParameterCount()];
                    final Annotation[][] paraAnnos = method.getParameterAnnotations();


                    //Mapper constructor
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE_P: " + key + " -> " + method);
                    jsonBuilders.put(key, new JsonBuilderMethod(clazz, key, method, mappers, jsonConstructor.useConstructorData()));
                }
                else
                {
                    //Default constructor
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE_D: " + key + " -> " + method);
                    jsonBuilders.put(key, new JsonBuilderMethod(clazz, key, method, null, false));
                }

            }
        }

        //Handle Constructors
        final Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors)
        {
            final JsonConstructor jsonConstructor = (JsonConstructor) constructor.getAnnotation(JsonConstructor.class);
            if (jsonConstructor != null)
            {
                final String key = jsonConstructor.type().toLowerCase();

                if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].isAssignableFrom(JsonElement.class))
                {
                    //Json only constructor
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE_J: " + key + " -> " + constructor);
                    jsonBuilders.put(key, new JsonBuilderConstructor(clazz, key, constructor, null, false));
                }
                else if (constructor.getParameterCount() > 0)
                {
                    //Locate all mappings
                    final BiFunction<JsonObject, ConversionHandler, Object>[] mappers
                            = MapperHelpers.buildMappers(constructor.getParameterTypes(),
                            constructor.getParameterAnnotations(),
                            () -> " Class: " + clazz + " Constructor: " + constructor.toString());

                    //Mapper constructor
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE_P: " + key + " -> " + constructor);
                    jsonBuilders.put(key, new JsonBuilderConstructor(clazz, key, constructor, mappers, jsonConstructor.useConstructorData()));
                }
                else
                {
                    //Default constructor
                    System.out.println("JsonClassMapper[" + clazz + "] CREATE_D: " + key + " -> " + constructor);
                    jsonBuilders.put(key, new JsonBuilderConstructor(clazz, key, constructor, null, false));
                }
            }
        }

        final JsonTemplate jsonTemplate = (JsonTemplate) clazz.getAnnotation(JsonTemplate.class);
        if (jsonTemplate != null && jsonTemplate.useDefaultConstructor())
        {
            final String type = jsonTemplate.type().toLowerCase();
            jsonBuilders.put(type, new JsonBuilderSupplier(type, () ->
            {
                try
                {
                    return clazz.newInstance();
                } catch (Exception e)
                {
                    throw new RuntimeException("JsonClassMapper: Unexpected error using creating object of type[" + type + "] using default method for clazz " + clazz, e);

                }
            }));
        }

        return this;
    }

    public void addLink(String name, IJsonLinker linker)
    {
        name = name.toLowerCase();
        if (!linkMappers.containsKey(name))
        {
            linkMappers.put(name, new ArrayList());
        }
        linkMappers.get(name).add(linker);
    }

    public void mapDataFields(JsonObject json, Object objectToMap, ConversionHandler handler)
    {
        //Do parent class first to get allow child to replace parent data
        if (getParent() != null)
        {
            getParent().mapDataFields(json, objectToMap, handler);
        }

        //Do simple field mapping
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

        //Do more complex method mapping
        for(IJsonInjectionMapper mapper : injectionMappers)
        {
            mapper.map(objectToMap, json, handler);
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
                System.out.println("JsonClassMapper: trying " + linkMappers.get(key).size() + " linkers for field " + key);
                linkMappers.get(key).forEach(linker -> linker.link(object, data, registry));
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

    public void validate(Object object, boolean links)
    {
        if (!links)
        {
            mappings.values().forEach(mapper ->
            {
                if (!mapper.isValid(object))
                {
                    throw new RuntimeException("JsonClassMapper: Missing required field " + mapper);
                }
            });
        }
        else
        {
            linkMappers.values().forEach(list -> list.forEach(linker ->
            {
                if (!linker.isValid(object))
                {
                    throw new RuntimeException("JsonClassMapper: Missing required link " + linker);
                }
            }));
        }

        if (getParent() != null)
        {
            getParent().validate(object, links);
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
        IJsonBuilder builder = jsonBuilders.get(type.toLowerCase());
        if (builder == null)
        {
            builder = jsonBuilders.get(ContentBuilderRefs.ANY);
        }
        return builder;
    }

    public void destroy()
    {
        mappings.values().forEach(mappers -> mappers.destroy());
        mappings.clear();

        linkMappers.values().forEach(list -> list.forEach(mappers -> mappers.destroy()));
        linkMappers.clear();
    }
}

package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonFieldMapper extends JsonMapper<Object>
{

    private final Class clazz;
    private final Field field;
    private final boolean required;

    public JsonFieldMapper(Class clazz, Field field, JsonMapping mapping)
    {
        this(clazz, field, mapping.keys(), mapping.type(), mapping.args(), mapping.required());
    }

    public JsonFieldMapper(Class clazz, Field field, String[] names, String type, String[] args, boolean required)
    {
        super(names, type, args);
        this.clazz = clazz;
        this.field = field;
        this.required = required;
    }

    @Override
    public void map(Object objectToSetFieldOn, JsonElement data, ConversionHandler converter)
    {
        Object valueToSet = null;
        try
        {
            //Make public
            field.setAccessible(true);

            //Convert data
            valueToSet = converter.fromJson(type, data, args);

            //validate
            if (valueToSet != null) //TODO make throw errors if fails to generate instead of NPE check
            {
                //Special handling for collections
                if (type.equalsIgnoreCase(ConverterRefs.LIST)) //TODO create a handler set for special cases, interface on converter? or separate reg?
                {
                    if (Collection.class.isAssignableFrom(field.getType()))
                    {
                        final Collection storedValue = (Collection) field.get(objectToSetFieldOn);
                        if (storedValue != null)
                        {
                            //Add all
                            storedValue.addAll((Collection) valueToSet);

                            //Clear for better GC
                            ((Collection) valueToSet).clear();
                        }
                        else
                        {
                            throw new RuntimeException("JsonFieldMapper: Fields using 'LIST' conversion type must be"
                                    + " initialized for mapping to work as expected."
                                    + " CLASS: " + clazz
                                    + " FIELD: " + field);
                        }
                    }
                    //Extra way to apply collections
                    else if (Consumer.class.isAssignableFrom(field.getType()))
                    {
                        final Consumer consumer = (Consumer) field.get(objectToSetFieldOn);
                        if (consumer != null)
                        {
                            ((Collection) valueToSet).forEach(e -> consumer.accept(e));
                        }
                        else
                        {
                            throw new RuntimeException("JsonFieldMapper: Fields using 'LIST' conversion type must be"
                                    + " initialized for mapping to work as expected."
                                    + " CLASS: " + clazz
                                    + " FIELD: " + field);
                        }
                    }
                    else
                    {
                        throw new RuntimeException("JsonFieldMapper: Fields using 'LIST' conversion type must be"
                                + " of type Collection or a subclass of Collection such as List."
                                + " CLASS: " + clazz
                                + " FIELD: " + field);
                    }
                }
                else
                {
                    //Do Set
                    field.set(objectToSetFieldOn, valueToSet);
                }
            }
            else
            {
                throw new RuntimeException("JsonFieldMapper: Failed to generate object using "
                        + " TYPE: " + type
                        + " ARGS: " + args
                        + " JSON: " + data);
            }
        } catch (Exception e)
        {
            if (objectToSetFieldOn.getClass() != clazz || !objectToSetFieldOn.getClass().isAssignableFrom(clazz))
            {
                throw new RuntimeException("JsonMethodMapper: Failed to map json data to field due to it's class not matching. "
                        + "\n FIELD:    " + field.getName() + "  T:" + field.getType()
                        + "\n TYPE:     " + type
                        + "\n ARGS:     " + Arrays.toString(args)
                        + "\n OBJ:      " + objectToSetFieldOn
                        + "\n VALUE:    " + valueToSet + "  C: " + (valueToSet != null ? valueToSet.getClass() : "Nil")
                        + "\n JSON:     " + data,
                        e);
            }
            throw new RuntimeException("JsonMethodMapper: Failed to map json data to field. "
                    + "\n FIELD:    " + field.getName() + "  T:" + field.getType()
                    + "\n TYPE:     " + type
                    + "\n ARGS:     " + Arrays.toString(args)
                    + "\n OBJ:      " + objectToSetFieldOn
                    + "\n VALUE:    " + valueToSet + "  C: " + (valueToSet != null ? valueToSet.getClass() : "Nil")
                    + "\n JSON:     " + data,
                    e);
        }
    }

    @Override
    public boolean isValid(Object object)
    {
        try
        {
            return !required || field.get(object) != null;
        } catch (IllegalAccessException e)
        {
            return false;
        }
    }
}

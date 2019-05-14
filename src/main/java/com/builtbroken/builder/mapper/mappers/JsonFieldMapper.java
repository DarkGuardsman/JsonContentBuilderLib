package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.Arrays;

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
            field.setAccessible(true);

            valueToSet = converter.fromJson(type, data, args);
            //TODO make throw errors if fails to generate instead of NPE check
            if (valueToSet != null)
            {
                field.set(objectToSetFieldOn, valueToSet);
            }
            else
            {
                throw new RuntimeException("Failed to generate object");
            }
        } catch (Exception e)
        {
            if(objectToSetFieldOn.getClass() != clazz || !objectToSetFieldOn.getClass().isAssignableFrom(clazz))
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

package com.builtbroken.builder.mapper.mappers;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.mapper.JsonMapping;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class JsonFieldMapper extends JsonMapper<Object>
{

    private final Field field;
    private final boolean required;

    public JsonFieldMapper(Field field, JsonMapping mapping)
    {
        this(field, mapping.value(), mapping.type(), mapping.args(), mapping.required());
    }

    public JsonFieldMapper(Field field, String[] names, String type, String[] args, boolean required)
    {
        super(names, type, args);
        this.field = field;
        this.required = required;
    }

    @Override
    public void map(Object object, JsonElement data, ConversionHandler converter)
    {

        try
        {
            field.setAccessible(true);

            final Object generatedObject = converter.fromJson(type, data, args);
            //TODO make throw errors if fails to generate instead of NPE check
            if (generatedObject != null)
            {
                field.set(object, generatedObject);
            }
            else
            {
                throw new RuntimeException("Failed to generate object");
            }
        } catch (Exception e)
        {
            throw new RuntimeException("JsonMethodMapper: Failed to map json data to " +
                    "field " + field.getName()
                    + "\n TYPE: " + type
                    + "\n ARGS: " + Arrays.toString(args)
                    + "\n OBJ: " + object
                    + "\n JSON: " + data);
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

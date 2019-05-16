package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.mapper.MapperHelpers;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.mappers.JsonMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public abstract class JsonBuilderMapper extends JsonBuilder
{

    protected final Class clazz;

    protected final BiFunction<JsonObject, ConversionHandler, Object>[] mappers;
    protected final boolean useConstructorData;

    public JsonBuilderMapper(Class clazz, String type, BiFunction<JsonObject, ConversionHandler, Object>[] mappers, boolean useConstructorData)
    {
        super(type);
        this.clazz = clazz;
        this.mappers = mappers;
        this.useConstructorData = useConstructorData;
    }

    @Override
    public String getUniqueID()
    {
        return type;
    }

    /**
     * Maps the parameters to an array ready for input into creation method
     *
     * @param converter - handler to process the json
     * @param object    - json data to read
     * @return array of mapped data from the json
     */
    protected Object[] buildParameters(ConversionHandler converter, JsonObject object)
    {
        //Get json object, if set to use constructor data select the object
        final JsonObject jsonObject = !useConstructorData
                ? object
                : object.get(ContentBuilderRefs.JSON_CONSTRUCTOR).getAsJsonObject();

       return MapperHelpers.buildInputs(jsonObject, converter, mappers);
    }


}

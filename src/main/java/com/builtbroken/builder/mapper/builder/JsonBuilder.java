package com.builtbroken.builder.mapper.builder;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonObject;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public abstract class JsonBuilder implements IJsonBuilder
{

    protected final Class clazz;
    protected final String type;
    protected final JsonMapping[] mappers;
    protected final boolean useConstructorData;

    public JsonBuilder(Class clazz, String type, JsonMapping[] mappers, boolean useConstructorData)
    {
        this.clazz = clazz;
        this.type = type;
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
        final JsonObject jsonObject = useConstructorData
                ? object
                : object.get(ContentBuilderRefs.CONSTRUCTOR_DATA).getAsJsonObject();

        //Map args
        final Object[] args = new Object[mappers.length];
        for (int i = 0; i < mappers.length; i++)
        {
            final JsonMapping mapper = mappers[i];
            for (String key : mapper.keys())
            {
                args[i] = converter.fromJson(mapper.type(), jsonObject.get(key), mapper.args());
                if (args[i] != null)
                {
                    break;
                }
            }
        }

        return args;
    }
}

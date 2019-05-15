package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Used to store information about a file when created by an editor or auto generation program
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_CREATION_DATA)
public class CreationData implements IJsonGeneratedObject
{

    public String name;

    public MetaDataLevel level;

    @JsonMapping(keys = "creation_date", type = ConverterRefs.STRING)
    public String createdOn;

    @JsonMapping(keys = "last_update_date", type = ConverterRefs.STRING)
    public String updatedLast;

    @JsonMapping(keys = "program_used", type = ConverterRefs.STRING)
    public String program;

    @JsonMapping(keys = "version", type = ConverterRefs.STRING)
    public String version;

    @JsonConstructor
    public static CreationData create(@JsonMapping(keys = "name", type = ConverterRefs.STRING, required = true) String name,
                                      @JsonMapping(keys = "level", type = ConverterRefs.ENUM, required = true) MetaDataLevel level)
    {
        CreationData creationData = new CreationData();
        creationData.name = name;
        creationData.level = level;

        return creationData;
    }

    @Override
    public String getJsonType()
    {
        return ContentBuilderRefs.TYPE_CREATION_DATA + "." + level.name().toLowerCase();
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

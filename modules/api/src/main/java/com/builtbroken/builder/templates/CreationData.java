package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Used to store information about a file when created by an editor or auto generation program
 * <p>
 * Created by Robin Seifert on 2019-05-14.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_CREATION_DATA)
public class CreationData implements IJsonGeneratedObject
{

    public String id;

    public MetaDataLevel level;

    @JsonMapping(keys = "creation_date", type = ConverterRefs.STRING)
    public String createdOn;

    @JsonMapping(keys = "last_update_date", type = ConverterRefs.STRING)
    public String updatedLast;

    @JsonMapping(keys = "program_used", type = ConverterRefs.STRING)
    public String program;

    @JsonObjectWiring(jsonFields = "id", objectType = "version", valuePrefix = ContentBuilderRefs.TYPE_CREATION_DATA + ":")
    public VersionData version;

    @JsonConstructor
    public static CreationData create(@JsonMapping(keys = "id", type = ConverterRefs.STRING, required = true) String id,
                                      @JsonMapping(keys = "level", type = ConverterRefs.ENUM, required = true) MetaDataLevel level)
    {
        CreationData creationData = new CreationData();
        creationData.id = id;
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
        return id;
    }

    @Override
    public String toString()
    {
        return "CreationData[" + getJsonUniqueID() + "]";
    }
}

package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_VERSION_DATA)
public class VersionData implements IJsonGeneratedObject
{
    @JsonMapping(keys = "name", type = ConverterRefs.STRING, required = true)
    public String name;

    @JsonMapping(keys = "level", type = ConverterRefs.ENUM, required = true)
    public MetaDataLevel level;

    @JsonMapping(keys = "version", type = ConverterRefs.STRING, required = true)
    public String version;

    @JsonConstructor
    public static VersionData create(@JsonMapping(keys = "name", type = ConverterRefs.STRING, required = true) String name,
                                    @JsonMapping(keys = "level", type = ConverterRefs.ENUM, required = true) MetaDataLevel type)
    {
        VersionData data = new VersionData();
        data.name = name;
        data.level = type;
        return data;
    }

    @Override
    public String getJsonType()
    {
        return ContentBuilderRefs.TYPE_VERSION_DATA + "." + level.name().toLowerCase();
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

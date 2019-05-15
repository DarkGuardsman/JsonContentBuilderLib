package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Used to store information about a file when created by an editor or auto generation program
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_CREATION_DATA, useDefaultConstructor = true)
public class CreationData implements IJsonGeneratedObject
{

    @JsonMapping(keys = "name", type = ConverterRefs.STRING, required = true)
    public String name;

    @JsonMapping(keys = "meta_type", type = ConverterRefs.ENUM, required = true)
    public MetaDataType type;

    @JsonMapping(keys = "creation_date", type = ConverterRefs.STRING)
    public String createdOn;

    @JsonMapping(keys = "last_update_date", type = ConverterRefs.STRING)
    public String updatedLast;

    @JsonMapping(keys = "program_used", type = ConverterRefs.STRING)
    public String program;

    @JsonMapping(keys = "version", type = ConverterRefs.STRING)
    public String version;

    @Override
    public String getJsonType()
    {
        return ContentBuilderRefs.TYPE_CREATION_DATA + "." + type.name().toLowerCase();
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

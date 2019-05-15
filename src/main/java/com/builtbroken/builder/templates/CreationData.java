package com.builtbroken.builder.templates;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Used to store information about a file when created by an editor or auto generation program
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = "metadata", useDefaultConstructor = true)
public class CreationData implements IJsonGeneratedObject
{

    @JsonMapping(keys = "name", type = "string", required = true)
    public String name;

    @JsonMapping(keys = "meta_type", type = "enum", required = true)
    public MetaDataType type;

    @JsonMapping(keys = "creation_date", type = "string")
    public String createdOn;

    @JsonMapping(keys = "last_update_date", type = "string")
    public String updatedLast;

    @JsonMapping(keys = "program_used", type = "string")
    public String program;

    @JsonMapping(keys = "version", type = "string")
    public String version;

    @Override
    public String getJsonType()
    {
        return "metadata." + type.name().toLowerCase();
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

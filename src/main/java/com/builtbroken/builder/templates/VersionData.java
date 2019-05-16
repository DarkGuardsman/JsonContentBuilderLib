package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.data.ISimpleDataValidation;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_VERSION_DATA)
public class VersionData implements IJsonGeneratedObject, ISimpleDataValidation
{

    public String id;

    public MetaDataLevel level;

    @JsonMapping(keys = "version", type = ConverterRefs.STRING, required = true)
    public String version;

    @JsonConstructor
    public static VersionData create(@JsonMapping(keys = "id", type = ConverterRefs.STRING, required = true) String name,
                                     @JsonMapping(keys = "level", type = ConverterRefs.ENUM) MetaDataLevel type)
    {
        VersionData data = new VersionData();
        data.id = name;
        data.level = type;
        if (data.level == null)
        {
            data.level = MetaDataLevel.OBJECT;
        }
        return data;
    }

    @Override
    public String getJsonType()
    {
        if (level == null || level == MetaDataLevel.OBJECT)
        {
            return ContentBuilderRefs.TYPE_VERSION_DATA;
        }
        return ContentBuilderRefs.TYPE_VERSION_DATA + "." + level.name().toLowerCase();
    }

    @Override
    public String getJsonUniqueID()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "VersionData[" + getJsonUniqueID() + "]";
    }

    @Override
    public boolean isValid()
    {
        if (level != null && id != null && !id.isEmpty())
        {
            //Object metadata requires that the ID be a link to another object
            //   EX: armory.sword:copper
            //   First half is the template's type ID
            //   Second half is the unique ID within the type
            if (level == MetaDataLevel.OBJECT)
            {
                final String[] split = id.split(":");
                if (split.length == 2)
                {
                    return !split[0].trim().isEmpty()
                            && !split[1].trim().isEmpty();
                    //TODO find a way to validate the split 0 is a valid template
                }
                return false;
            }
            return true;
        }
        return false;
    }
}

package com.builtbroken.builder.templates;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.data.ISimpleDataValidation;

/**
 * Created by Robin Seifert on 6/26/2021.
 */
public  abstract class AbstractLevelData implements IJsonGeneratedObject, ISimpleDataValidation
{
    protected String id;
    protected MetaDataLevel level;

    @Override
    public String getJsonUniqueID()
    {
        return id;
    }

    public MetaDataLevel getMetaDataLevel() {
        return level;
    }

    @Override
    public String getJsonRegistryID()
    {
        if (level == null || level == MetaDataLevel.OBJECT)
        {
            return getJsonTemplateID();
        }
        return getJsonTemplateID() + "." + level.name().toLowerCase();
    }

    @Override
    public boolean isValid()
    {
        return level != null
                && id != null && !id.isEmpty();
    }
}

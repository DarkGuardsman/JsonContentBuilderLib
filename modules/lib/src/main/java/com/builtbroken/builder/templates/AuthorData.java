package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.data.ISimpleDataValidation;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Used to store author information about a folder containing JSON or a single json
 * <p>
 * Created by Robin Seifert on 2019-05-14.
 */
@JsonTemplate(ContentBuilderRefs.TYPE_AUTHOR_DATA)
public class AuthorData extends AbstractLevelData implements IJsonGeneratedObject, ISimpleDataValidation
{
    /**
     * Author's name for display
     */
    @JsonMapping(keys = "name", type = ConverterRefs.STRING, required = true)
    private String name;

    /**
     * Author's personal URL or contract information
     */
    @JsonMapping(keys = "url", type = ConverterRefs.STRING)
    private String url;

    @JsonConstructor
    public static AuthorData create(@JsonMapping(keys = "id", type = ConverterRefs.STRING, required = true) String id,
                                    @JsonMapping(keys = "level", type = ConverterRefs.ENUM, required = true) MetaDataLevel type)
    {
        AuthorData data = new AuthorData();
        data.id = id;
        data.level = type;
        return data;
    }

    @Override
    public String getJsonTemplateID()
    {
        return ContentBuilderRefs.TYPE_AUTHOR_DATA;
    }

    @Override
    public String getJsonUniqueID()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "AuthorData[" + getJsonUniqueID() + "]";
    }

    @Override
    public boolean isValid()
    {
        return super.isValid() && name != null && !name.isEmpty();
    }

    public String getAuthorName()
    {
        return name;
    }

    public String getAuthorUrl()
    {
        return url;
    }
}

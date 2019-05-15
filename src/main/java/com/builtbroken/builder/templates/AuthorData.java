package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Used to store author information about a folder containing JSON or a single json
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_AUTHOR_DATA, useDefaultConstructor = true)
public class AuthorData implements IJsonGeneratedObject
{

    /**
     * Author's name
     */
    @JsonMapping(keys = "name", type = ConverterRefs.STRING, required = true)
    public String name;

    /**
     * Group of the author data. For a single file this will be null, for package it will
     * be the unique package name, and for project it will be the project's name.
     */
    @JsonMapping(keys = "group", type = ConverterRefs.STRING)
    public String group;

    /**
     * Author's personal URL or contract information
     */
    @JsonMapping(keys = "url", type = ConverterRefs.STRING)
    public String url;

    /**
     * Type of author data used to track if this is for a single file, package, or project
     */
    @JsonMapping(keys = "meta_type", type = ConverterRefs.STRING, required = true)
    public MetaDataType type;

    @Override
    public String getJsonType()
    {
        return ContentBuilderRefs.TYPE_AUTHOR_DATA + "." + type;
    }

    @Override
    public String getJsonUniqueID()
    {
        return group != null ? group : name;
    }
}

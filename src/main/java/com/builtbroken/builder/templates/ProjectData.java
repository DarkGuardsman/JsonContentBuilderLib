package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to track a collection of JSON as a project for an editor or program to load
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
@JsonTemplate(type = ContentBuilderRefs.TYPE_PROJECT_DATA, useDefaultConstructor = true)
public class ProjectData implements IJsonGeneratedObject
{

    /**
     * Unique name of the project
     */
    @JsonMapping(keys = {"name", "id"}, type = ConverterRefs.STRING, required = true)
    public String name;

    /**
     * Display name of the project
     */
    @JsonMapping(keys = "display_name", type = ConverterRefs.STRING)
    public String displayName;

    /**
     * Creation information about the project
     */
    @JsonObjectWiring(jsonFields = "name", objectType = ContentBuilderRefs.TYPE_CREATION_DATA + "." + ContentBuilderRefs.TYPE_PROJECT_DATA)
    public CreationData creationData;

    /**
     * Person who created the project
     */
    @JsonObjectWiring(jsonFields = "name", objectType = ContentBuilderRefs.TYPE_AUTHOR_DATA + "." + ContentBuilderRefs.TYPE_PROJECT_DATA)
    public AuthorData authorData;

    /**
     * Files or folders to include as part of the project
     */
    @JsonMapping(keys = "include", type = ConverterRefs.LIST)
    public List<String> includePaths = new ArrayList();

    /**
     * Files or folders to exclude from the project
     */
    @JsonMapping(keys = "exclude", type = ConverterRefs.LIST)
    public List<String> excludePaths = new ArrayList();

    @Override
    public String getJsonType()
    {
        return ContentBuilderRefs.TYPE_PROJECT_DATA;
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

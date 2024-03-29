package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to track a collection of JSON as a project for an editor or program to load
 * <p>
 * Created by Robin Seifert on 2019-05-15.
 */
@JsonTemplate(ContentBuilderRefs.TYPE_PROJECT_DATA)
public class ProjectData implements IJsonGeneratedObject
{
    /**
     * Unique id of the project
     */
    public String id;

    /**
     * Display name of the project
     */
    @JsonMapping(keys = "display_name", type = ConverterRefs.STRING)
    public String displayName;

    /**
     * Creation information about the project
     */
    @JsonObjectWiring(jsonFields = "id", objectType = ContentBuilderRefs.TYPE_CREATION_DATA + ".project")
    public ObjectMetadata creationData;

    /**
     * Person who created the project
     */
    @JsonObjectWiring(jsonFields = "id", objectType = ContentBuilderRefs.TYPE_AUTHOR_DATA + ".project")
    public AuthorData authorData;

    /**
     * Files or folders to include as part of the project
     */
    @JsonMapping(keys = "include", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
    public final List<String> includePaths = new ArrayList();

    /**
     * Files or folders to exclude from the project
     */
    @JsonMapping(keys = "exclude", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
    public final List<String> excludePaths = new ArrayList();

    @JsonConstructor
    public static ProjectData create( @JsonMapping(keys = "id", type = ConverterRefs.STRING, required = true) String name)
    {
        ProjectData projectData = new ProjectData();
        projectData.id = name;
        return projectData;
    }

    @Override
    public String getJsonTemplateID()
    {
        return ContentBuilderRefs.TYPE_PROJECT_DATA;
    }

    @Override
    public String getJsonUniqueID()
    {
        return id;
    }


    @Override
    public String toString()
    {
        return "ProjectData[" + getJsonUniqueID() + "]";
    }
}

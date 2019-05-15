package com.builtbroken.builder.templates;

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
@JsonTemplate(type = "project")
public class ProjectData implements IJsonGeneratedObject
{

    /**
     * Unique name of the project
     */
    @JsonMapping(keys = {"name", "id"}, type = "string", required = true)
    public String name;

    /**
     * Display name of the project
     */
    @JsonMapping(keys = "display_name", type = "string")
    public String displayName;

    /**
     * Creation information about the project
     */
    @JsonObjectWiring(jsonFields = "name", objectType = "metadata.project")
    public CreationData creationData;

    /**
     * Person who created the project
     */
    @JsonObjectWiring(jsonFields = "name", objectType = "author.project")
    public AuthorData authorData;

    /**
     * Files or folders to include as part of the project
     */
    @JsonMapping(keys = "include", type = "list")
    public List<String> includePaths = new ArrayList();

    /**
     * Files or folders to exclude from the project
     */
    @JsonMapping(keys = "exclude", type = "list")
    public List<String> excludePaths = new ArrayList();

    @Override
    public String getJsonType()
    {
        return "project";
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

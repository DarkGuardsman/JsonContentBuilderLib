package com.builtbroken.builder.templates;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = "author")
public class AuthorData implements IJsonGeneratedObject
{

    public final String name;
    public final String url;

    //exists for service loader
    public AuthorData()
    {
        name = "service";
        url = "local";
    }

    @JsonConstructor
    public AuthorData(@JsonMapping(keys = "name", type = "string") String name, @JsonMapping(keys = "url", type = "string") String url)
    {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getJsonType()
    {
        return "author";
    }

    @Override
    public String getJsonUniqueID()
    {
        return name;
    }
}

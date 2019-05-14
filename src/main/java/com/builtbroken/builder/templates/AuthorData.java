package com.builtbroken.builder.templates;

import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = "author")
public class AuthorData
{

    public final String name;
    public final String url;

    @JsonConstructor
    public AuthorData(@JsonMapping(keys = "name", type = "string") String name, @JsonMapping(keys = "url", type = "string") String url)
    {
        this.name = name;
        this.url = url;
    }
}

package com.builtbroken.builder.templates;

import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
@JsonTemplate(type = "metadata", useDefaultConstructor = true)
public class CreationData
{

    @JsonMapping(keys = "creation_date", type = "string")
    public String createdOn;

    @JsonMapping(keys = "last_update_date", type = "string")
    public String updatedLast;

    @JsonMapping(keys = "program_used", type = "string")
    public String program;

    @JsonMapping(keys = "version", type = "string")
    public String version;
}

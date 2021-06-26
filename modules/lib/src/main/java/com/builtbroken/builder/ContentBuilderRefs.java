package com.builtbroken.builder;

/**
 * Created by Robin Seifert on 2/26/19.
 */
public class ContentBuilderRefs
{

    //==== PIPE IDs ======
    public static final String PIPE_JSON = "json";
    public static final String PIPE_ID = "com.builtbroken";
    public static final String PIPE_COMMENT_REMOVER = PIPE_ID + ":json.comment.remover";
    public static final String PIPE_JSON_SPLITTER = PIPE_ID + ":json.splitter";


    public static final String PIPE_BUILDER = "builder";
    public static final String PIPE_BUILDER_CREATION = PIPE_ID + ":creator";


    public static final String PIPE_MAPPER = "mapper";
    public static final String PIPE_MAPPER_FIELDS = PIPE_MAPPER + ":fields";
    public static final String PIPE_MAPPER_INJECTION = PIPE_MAPPER + ":injection"; //TODO implement
    public static final String PIPE_MAPPER_VALIDATION = PIPE_MAPPER + ":validation";
    public static final String PIPE_MAPPER_OBJECT_REG = PIPE_MAPPER + ":registration";

    public static final String PIPE_POST = "post";
    public static final String PIPE_POST_WIRING = PIPE_MAPPER + ":wires";
    public static final String PIPE_POST_WIRING_VALIDATION = PIPE_MAPPER + ":validation.wires";
    public static final String PIPE_POST_VALIDATION = PIPE_MAPPER + ":validation";

    //==== JSON IDs ======
    public static final String JSON_CONSTRUCTOR = "constructor";
    public static final String JSON_TYPE = "type"; //Can be changed per loader
    public static final String JSON_DATA = "data"; //Can be changed per loader

    //==== IDS ======
    public static final String MAIN_LOADER = "main";

    //==== JSON Object Types used by project
    public static final String TYPE_AUTHOR_DATA = "builder:author";
    public static final String TYPE_CREATION_DATA = "builder:metadata";
    public static final String TYPE_PROJECT_DATA = "builder:project";
    public static final String TYPE_VERSION_DATA = "builder:version";

    //==== DATA =====
    public static final String ANY = "*";
}

package com.builtbroken.tests.loader;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class MainContentLoaderTest
{
    @BeforeEach
    public void beforeEach()
    {

    }

    @AfterEach
    public void afterEach()
    {
        ContentBuilderLib.destroy();
    }

    @Test
    void loader_validatePipeline()
    {
        final MainContentLoader loader = new MainContentLoader();

        //Build array for testing
        final JsonArray array = generateJson();

        //Fake file loader
        loader.addFileLocator(() -> Collections.singleton(new DataFileLoad(new File("./fake.json"), array)));

        //Setup loader and add registry templates
        loader.registerObjectTemplate("test:tree","test:tree", TreeTest.class, (json) -> new TreeTest());
        loader.registerObjectTemplate("test:log","test:log", LogTest.class, (json) -> new LogTest());
        loader.registerObjectTemplate("test:stick","test:stick", StickTest.class, (json) -> new StickTest());
        loader.setup();
        loader.load();

        //We should have generated 3 objects
        Assertions.assertEquals(3, loader.generatedObjects.size());

        final TreeTest tree = (TreeTest) ((GeneratedObject) loader.generatedObjects.get(0)).objectCreated;
        final LogTest log = (LogTest) ((GeneratedObject) loader.generatedObjects.get(1)).objectCreated;
        final StickTest stick = (StickTest) ((GeneratedObject) loader.generatedObjects.get(2)).objectCreated;

        //Validate we mapped each field
        Assertions.assertEquals(0, tree.i);
        Assertions.assertEquals(1, log.i);
        Assertions.assertEquals(2, stick.i);

        //Validate we wired fields together
        Assertions.assertNotNull(tree.log);
        Assertions.assertNotNull(log.stick);

    }

    private static JsonArray generateJson()
    {
        JsonArray array = new JsonArray();

        JsonObject object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "test:tree");
        object.addProperty("child", "log3");
        object.addProperty("i", 0);
        array.add(object);


        object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "test:log");
        object.addProperty("child", "stick5");
        object.addProperty("i", 1);
        array.add(object);


        object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "test:stick");
        object.addProperty("i", 2);
        array.add(object);

        return array;
    }

    public static class TreeTest implements IJsonGeneratedObject
    {

        @JsonMapping(keys = "i", type = "int")
        public int i;

        @JsonObjectWiring(jsonFields = "child", objectType = "log")
        public LogTest log;

        @Override
        public String getJsonTemplateID()
        {
            return "tree";
        }

        @Override
        public String getJsonUniqueID()
        {
            return "tree2";
        }
    }

    public static class LogTest implements IJsonGeneratedObject
    {

        @JsonMapping(keys = "i", type = "int")
        public int i;

        @JsonObjectWiring(jsonFields = "child", objectType = "stick")
        public StickTest stick;

        @Override
        public String getJsonTemplateID()
        {
            return "log";
        }

        @Override
        public String getJsonUniqueID()
        {
            return "log3";
        }
    }

    public static class StickTest implements IJsonGeneratedObject
    {

        @JsonMapping(keys = "i", type = "int")
        public int i;

        @Override
        public String getJsonTemplateID()
        {
            return "stick";
        }

        @Override
        public String getJsonUniqueID()
        {
            return "stick5";
        }
    }
}

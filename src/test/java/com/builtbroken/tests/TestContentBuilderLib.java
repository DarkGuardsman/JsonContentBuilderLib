package com.builtbroken.tests;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.builtbroken.tests.pipe.TestPipeLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class TestContentBuilderLib
{
    @Test
    public void testInit()
    {
        //Check methods for getting main loader and convert, technically loader created converter
        Assertions.assertNotNull(ContentBuilderLib.getMainLoader());
        Assertions.assertNotNull(ContentBuilderLib.getMainConverter());

        //Destroy
        ContentBuilderLib.destroy();

        //Run again, and this time test fields in main loader
        Assertions.assertNotNull(ContentBuilderLib.getMainLoader());
        Assertions.assertNotNull(ContentBuilderLib.getMainLoader().conversionHandler);
        Assertions.assertNotNull(ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry);
        Assertions.assertNotNull(ContentBuilderLib.getMainLoader().pipeLine);
    }

    @Test
    public void testFullRun()
    {
        //init loader
        final File file = new File(System.getProperty("user.dir"), "src/test/resources/test/full_load_test");
        ContentBuilderLib.getMainLoader().addFileLocator(new FileLocatorSimple(file));

        ContentBuilderLib.getMainLoader().registerObject("tree", TreeTest.class, (json) -> new TreeTest());
        ContentBuilderLib.getMainLoader().registerObject("log", LogTest.class, (json) -> new LogTest());
        ContentBuilderLib.getMainLoader().registerObject("stick", StickTest.class, (json) -> new StickTest());

        //Setup and run
        ContentBuilderLib.getMainLoader().setup();
        ContentBuilderLib.getMainLoader().load();

        //check
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesLocated);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesProcessed);
        Assertions.assertEquals(6, ContentBuilderLib.getMainLoader().objectsGenerated);


        //Cleanup
        ContentBuilderLib.destroy();
    }

    @Test
    public void testDestroy()
    {
        ContentBuilderLib.getMainLoader().setup();
        ContentBuilderLib.destroy();

        //assertEquals(0, ContentBuilderLib.getMainLoader().pipeLine.pipes.size());
        //assertEquals(0, ContentBuilderLib.getMainLoader().pipeLine.id_to_pipe.size());
        Assertions.assertEquals(0, ContentBuilderLib.getMainLoader().conversionHandler.getConverters().size());
    }

    private static abstract class TreeComponent  implements IJsonGeneratedObject
    {
        @JsonMapping(keys = "i", type = "int")
        public int i;

        @JsonMapping(keys = "name", type = "string")
        public String name;

        @Override
        public String getJsonUniqueID()
        {
            return name + "_" + i;
        }
    }

    public static class TreeTest extends TreeComponent
    {
        @JsonObjectWiring(jsonFields = "child", objectType = "log")
        public TestPipeLine.LogTest log;

        @Override
        public String getJsonType()
        {
            return "tree";
        }
    }

    public static class LogTest extends TreeComponent
    {
        @JsonObjectWiring(jsonFields = "child", objectType = "stick")
        public TestPipeLine.StickTest stick;

        @Override
        public String getJsonType()
        {
            return "log";
        }
    }

    public static class StickTest extends TreeComponent
    {
        @Override
        public String getJsonType()
        {
            return "stick";
        }
    }
}

package com.builtbroken.tests;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.IJsonConverter;
import com.builtbroken.builder.loader.FileLocatorSimple;
import com.builtbroken.tests.pipe.TestPipeLine;
import com.google.gson.JsonElement;
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

        ContentBuilderLib.getMainLoader().registerObject("tree", TestPipeLine.TreeTest.class, (json) -> new TestPipeLine.TreeTest());


        //Setup and run
        ContentBuilderLib.getMainLoader().setup();
        ContentBuilderLib.getMainLoader().load();

        //check
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesLocated);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesProcessed);
        //Assertions.assertEquals(6, ContentBuilderLib.getMainLoader().filesLocated);


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
}

package com.builtbroken.tests;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class TestContentBuilderLib extends TestCase
{
    @Test
    public void testInit()
    {
        //Check methods for getting main loader and convert, technically loader created converter
        assertNotNull(ContentBuilderLib.getMainLoader());
        assertNotNull(ContentBuilderLib.getMainConverter());

        //Destroy
        ContentBuilderLib.destroy();

        //Run again, and this time test fields in main loader
        assertNotNull(ContentBuilderLib.getMainLoader());
        assertNotNull(ContentBuilderLib.getMainLoader().conversionHandler);
        assertNotNull(ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry);
        assertNotNull(ContentBuilderLib.getMainLoader().pipeLine);
    }

    @Test
    public void testDestroy()
    {
        ContentBuilderLib.setup();
        ContentBuilderLib.destroy();

        //assertEquals(0, ContentBuilderLib.getMainLoader().pipeLine.pipes.size());
        //assertEquals(0, ContentBuilderLib.getMainLoader().pipeLine.id_to_pipe.size());
        assertEquals(0, ContentBuilderLib.getMainLoader().conversionHandler.getConverters().size());
    }
}

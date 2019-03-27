package com.builtbroken.tests;

import com.builtbroken.builder.ContentBuilderLib;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void testDestroy()
    {
        ContentBuilderLib.setup();
        ContentBuilderLib.destroy();

        //assertEquals(0, ContentBuilderLib.getMainLoader().pipeLine.pipes.size());
        //assertEquals(0, ContentBuilderLib.getMainLoader().pipeLine.id_to_pipe.size());
        Assertions.assertEquals(0, ContentBuilderLib.getMainLoader().conversionHandler.getConverters().size());
    }
}

package com.builtbroken.tests.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.AuthorData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public class TestAuthorData
{
    @Test
    public void testFileData()
    {
        //Setup
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/author/author_file.json");
        ContentBuilderLib.getMainLoader().addFileLocator(new FileLocatorSimple(file));
        ContentBuilderLib.getMainLoader().registerObjectTemplate(AuthorData.class);
        ContentBuilderLib.getMainLoader().setup();

        //Trigger loading of file
        ContentBuilderLib.getMainLoader().load();

        //Test we loaded something
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesLocated);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesProcessed);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.getHandler("author.file");
        Object object = handler.getObject("test author");

        Assertions.assertTrue(object instanceof AuthorData);


        //Cleanup
        ContentBuilderLib.getMainLoader().destroy();
    }
}

package com.builtbroken.tests.templates;

import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.AuthorData;
import com.builtbroken.builder.templates.MetaDataLevel;
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
        final MainContentLoader loader = new MainContentLoader();
        final File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/author/author_file.json");
        loader.addFileLocator(new FileLocatorSimple(file));
        loader.registerObjectTemplate(AuthorData.class);
        loader.setup();

        //Trigger loading of file
        loader.load();

        //Test we loaded something
        Assertions.assertEquals(1, loader.filesLocated);
        Assertions.assertEquals(1, loader.filesProcessed);
        Assertions.assertEquals(1, loader.objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler("author.file");
        Object object = handler.getObject("test.author");

        Assertions.assertTrue(object instanceof AuthorData, "Failed to locate author data");

        AuthorData authorData = (AuthorData) object;
        Assertions.assertNull(authorData.url);
        Assertions.assertEquals("Master Author", authorData.name);
        Assertions.assertEquals("test.author", authorData.id);
        Assertions.assertEquals(MetaDataLevel.FILE, authorData.level);
    }

    @Test
    public void testFolderData()
    {
        //Setup
        final MainContentLoader loader = new MainContentLoader();
        final File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/author/author_folder.json");
        loader.addFileLocator(new FileLocatorSimple(file));
        loader.registerObjectTemplate(AuthorData.class);
        loader.setup();

        //Trigger loading of file
        loader.load();

        //Test we loaded something
        Assertions.assertEquals(1, loader.filesLocated);
        Assertions.assertEquals(1, loader.filesProcessed);
        Assertions.assertEquals(1, loader.objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler("author.package");
        Object object = handler.getObject("test.data.author");

        Assertions.assertTrue(object instanceof AuthorData, "Failed to locate author data");

        AuthorData authorData = (AuthorData) object;
        Assertions.assertEquals("test.data.author", authorData.id);
        Assertions.assertEquals("Master Author", authorData.name);
        Assertions.assertEquals("www.builtbroken.com", authorData.url);
        Assertions.assertEquals(MetaDataLevel.PACKAGE, authorData.level);
    }
}

package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
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
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler(ContentBuilderRefs.TYPE_AUTHOR_DATA + ".file");
        Object object = handler.getObject("test.author");

        Assertions.assertTrue(object instanceof AuthorData, "Failed to locate author data");

        AuthorData authorData = (AuthorData) object;
        Assertions.assertNull(authorData.getAuthorUrl());
        Assertions.assertEquals("Master Author", authorData.getAuthorName());
        Assertions.assertEquals("test.author", authorData.getJsonUniqueID());
        Assertions.assertEquals(MetaDataLevel.FILE, authorData.getMetaDataLevel());
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
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler(ContentBuilderRefs.TYPE_AUTHOR_DATA + ".package");
        Object object = handler.getObject("test.data.author");

        Assertions.assertTrue(object instanceof AuthorData, "Failed to locate author data");

        AuthorData authorData = (AuthorData) object;
        Assertions.assertEquals("test.data.author", authorData.getJsonUniqueID());
        Assertions.assertEquals("Master Author", authorData.getAuthorName());
        Assertions.assertEquals("www.builtbroken.com", authorData.getAuthorUrl());
        Assertions.assertEquals(MetaDataLevel.PACKAGE, authorData.getMetaDataLevel());
    }
}

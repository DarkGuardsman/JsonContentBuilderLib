package com.builtbroken.tests.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.CreationData;
import com.builtbroken.builder.templates.MetaDataLevel;
import com.builtbroken.builder.templates.VersionData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public class TestCreationData
{

    @Test
    public void testFolderData()
    {
        //Setup
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/creation/metadata.json");
        ContentBuilderLib.getMainLoader().addFileLocator(new FileLocatorSimple(file));
        ContentBuilderLib.getMainLoader().registerObjectTemplate(CreationData.class);
        ContentBuilderLib.getMainLoader().registerObjectTemplate(VersionData.class);
        ContentBuilderLib.getMainLoader().setup();

        //Trigger loading of file
        ContentBuilderLib.getMainLoader().load();

        //Test we loaded something
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesLocated);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesProcessed);
        Assertions.assertEquals(2, ContentBuilderLib.getMainLoader().objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.getHandler("metadata.package");
        Assertions.assertNotNull(handler, "Failed to locate handler to get object");
        Object object = handler.getObject("test.meta");

        //Check that it exists
        Assertions.assertTrue(object instanceof CreationData, "Failed to locate object");

        CreationData creationData = (CreationData) object;

        //Validate creation data
        Assertions.assertNotNull(creationData.version, "Failed to wire version");
        Assertions.assertEquals(creationData.id, "test.meta");
        Assertions.assertEquals(MetaDataLevel.PACKAGE, creationData.level);
        Assertions.assertEquals(creationData.createdOn, "5/16/2019");
        Assertions.assertEquals(creationData.updatedLast, "5/17/2019");
        Assertions.assertEquals(creationData.program, "mac");

        //validate version
        VersionData versionData = creationData.version;
        Assertions.assertEquals("0.0.0.1", versionData.version);
        Assertions.assertEquals("metadata:test.meta", versionData.id);
        Assertions.assertEquals(MetaDataLevel.OBJECT, versionData.level);
    }

    @AfterEach
    public void afterEachTest()
    {
        ContentBuilderLib.destroy();
    }
}

package com.builtbroken.tests.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.ObjectMetadata;
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
        //Setup default loader
        final MainContentLoader loader = new MainContentLoader();
        final File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/creation/metadata.json");
        loader.addFileLocator(new FileLocatorSimple(file));
        loader.registerObjectTemplate(ObjectMetadata.class);
        loader.registerObjectTemplate(VersionData.class);
        loader.setup();

        //Trigger loading of file
        loader.load();

        //Test we loaded something
        Assertions.assertEquals(1, loader.filesLocated);
        Assertions.assertEquals(1, loader.filesProcessed);
        Assertions.assertEquals(2, loader.objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler(ContentBuilderRefs.TYPE_CREATION_DATA + ".package");
        Assertions.assertNotNull(handler, "Failed to locate handler to get object");
        Object object = handler.getObject("test.meta");

        //Check that it exists
        Assertions.assertTrue(object instanceof ObjectMetadata, "Failed to locate object");

        ObjectMetadata creationData = (ObjectMetadata) object;

        //Validate creation data
        Assertions.assertNotNull(creationData.version, "Failed to wire version");
        Assertions.assertEquals(creationData.id, "test.meta");
        Assertions.assertEquals(MetaDataLevel.PACKAGE, creationData.level);
        Assertions.assertEquals(creationData.createdOn, "5/16/2019");
        Assertions.assertEquals(creationData.updatedLast, "5/17/2019");
        Assertions.assertEquals(creationData.program, "mac");

        //validate version
        VersionData versionData = creationData.version;
        Assertions.assertEquals("0.0.0.1", versionData.getVersion());
        Assertions.assertEquals("test.meta", versionData.getJsonUniqueID());
        Assertions.assertEquals(MetaDataLevel.OBJECT, versionData.getMetaDataLevel());
    }

    @AfterEach
    public void afterEachTest()
    {
        ContentBuilderLib.destroy();
    }
}

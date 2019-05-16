package com.builtbroken.tests.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.AuthorData;
import com.builtbroken.builder.templates.MetaDataLevel;
import com.builtbroken.builder.templates.VersionData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public class TestVersionData
{

    @Test
    public void testFolderData()
    {
        //Setup
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/version/version.json");
        ContentBuilderLib.getMainLoader().addFileLocator(new FileLocatorSimple(file));
        ContentBuilderLib.getMainLoader().registerObjectTemplate(VersionData.class);
        ContentBuilderLib.getMainLoader().setup();

        //Trigger loading of file
        ContentBuilderLib.getMainLoader().load();

        //Test we loaded something
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesLocated);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesProcessed);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.getHandler("version.package");
        Object object = handler.getObject("version.test");

        Assertions.assertTrue(object instanceof VersionData);

        VersionData versionData = (VersionData) object;
        Assertions.assertEquals("0.0.1.1", versionData.version);
        Assertions.assertEquals("version.test", versionData.name);
        Assertions.assertEquals(MetaDataLevel.PACKAGE, versionData.level);

        //Cleanup
        ContentBuilderLib.destroy();
    }
}

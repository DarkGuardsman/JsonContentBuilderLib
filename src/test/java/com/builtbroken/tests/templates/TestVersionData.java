package com.builtbroken.tests.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.MetaDataLevel;
import com.builtbroken.builder.templates.VersionData;
import org.junit.jupiter.api.AfterEach;
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
        final ContentLoader loader = new MainContentLoader();
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/version/version.json");
        loader.addFileLocator(new FileLocatorSimple(file));
        loader.registerObjectTemplate(VersionData.class);
        loader.setup();

        //Trigger loading of file
        loader.load();

        //Test we loaded something
        Assertions.assertEquals(1, loader.filesLocated);
        Assertions.assertEquals(1, loader.filesProcessed);
        Assertions.assertEquals(1, loader.objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler("version.package");
        Object object = handler.getObject("version.test");

        Assertions.assertTrue(object instanceof VersionData);

        VersionData versionData = (VersionData) object;
        Assertions.assertEquals("0.0.1.1", versionData.version);
        Assertions.assertEquals("version.test", versionData.id);
        Assertions.assertEquals(MetaDataLevel.PACKAGE, versionData.level);
    }

    @AfterEach
    public void afterEachTest()
    {
        ContentBuilderLib.destroy();
    }
}

package com.builtbroken.tests.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public class TestProjectData
{

    @Test
    public void testFolderData()
    {
        //Setup
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/project/project.json");
        ContentBuilderLib.getMainLoader().addFileLocator(new FileLocatorSimple(file));
        ContentBuilderLib.getMainLoader().registerObjectTemplate(VersionData.class);
        ContentBuilderLib.getMainLoader().registerObjectTemplate(AuthorData.class);
        ContentBuilderLib.getMainLoader().registerObjectTemplate(CreationData.class);
        ContentBuilderLib.getMainLoader().registerObjectTemplate(ProjectData.class);
        ContentBuilderLib.getMainLoader().setup();

        //Trigger loading of file
        ContentBuilderLib.getMainLoader().load();

        //Test we loaded something
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesLocated);
        Assertions.assertEquals(1, ContentBuilderLib.getMainLoader().filesProcessed);
        Assertions.assertEquals(4, ContentBuilderLib.getMainLoader().objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.getHandler(ContentBuilderRefs.TYPE_PROJECT_DATA);
        Object object = handler.getObject("test.project");

        Assertions.assertTrue(object instanceof ProjectData);

        //Validate we exist with sub-objects
        ProjectData projectData = (ProjectData) object;
        Assertions.assertNotNull(projectData.authorData, "Failed to wire author data");
        Assertions.assertNotNull(projectData.creationData, "Failed to write creation data");
        Assertions.assertNotNull(projectData.creationData.version, "Failed to write version data");

        //Validate version stuff
        Assertions.assertEquals("0.0.1.1", projectData.creationData.version.version);
        Assertions.assertEquals("metadata:test.project", projectData.creationData.version.id);
        Assertions.assertEquals(MetaDataLevel.OBJECT, projectData.creationData.version.level);

        //Validate meta

        //Validate author

        //Validate project data
    }

    @AfterEach
    public void afterEachTest()
    {
        ContentBuilderLib.destroy();
    }
}
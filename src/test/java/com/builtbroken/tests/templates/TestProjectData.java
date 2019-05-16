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

        //Validate meta
        CreationData creationData = projectData.creationData;
        Assertions.assertEquals("test.project", creationData.id);
        Assertions.assertEquals(MetaDataLevel.PROJECT, creationData.level);

        //Validate version stuff
        VersionData versionData = creationData.version;
        Assertions.assertEquals("0.0.1.1", versionData.version);
        Assertions.assertEquals("metadata:test.project", versionData.id);
        Assertions.assertEquals(MetaDataLevel.OBJECT, versionData.level);

        //Validate author
        AuthorData authorData = projectData.authorData;
        Assertions.assertEquals("test.project", authorData.id);
        Assertions.assertEquals(MetaDataLevel.PROJECT, authorData.level);

        //Validate project data
        Assertions.assertEquals("test.project", projectData.id);
        Assertions.assertEquals("Test Project", projectData.displayName);

        Assertions.assertEquals(2, projectData.includePaths.size());
        Assertions.assertEquals("pathA", projectData.includePaths.get(0));
        Assertions.assertEquals("pathB", projectData.includePaths.get(1));

        Assertions.assertEquals(2, projectData.excludePaths.size());
        Assertions.assertEquals("pathC", projectData.excludePaths.get(0));
        Assertions.assertEquals("pathD", projectData.excludePaths.get(1));

    }

    @AfterEach
    public void afterEachTest()
    {
        ContentBuilderLib.destroy();
    }
}

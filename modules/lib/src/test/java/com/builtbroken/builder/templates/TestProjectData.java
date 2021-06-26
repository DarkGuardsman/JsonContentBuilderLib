package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
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
        final ContentLoader loader = new MainContentLoader();
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/data/project/project.json");
        loader.addFileLocator(new FileLocatorSimple(file));
        loader.registerObjectTemplate(VersionData.class);
        loader.registerObjectTemplate(AuthorData.class);
        loader.registerObjectTemplate(ObjectMetadata.class);
        loader.registerObjectTemplate(ProjectData.class);
        loader.setup();

        //Trigger loading of file
        loader.load();

        //Test we loaded something
        Assertions.assertEquals(1, loader.filesLocated);
        Assertions.assertEquals(1, loader.filesProcessed);
        Assertions.assertEquals(4, loader.objectsGenerated);

        //Test that our something is the right something
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler(ContentBuilderRefs.TYPE_PROJECT_DATA);
        Object object = handler.getObject("test.project");

        Assertions.assertTrue(object instanceof ProjectData);

        //Validate we exist with sub-objects
        ProjectData projectData = (ProjectData) object;
        Assertions.assertNotNull(projectData.authorData, "Failed to wire author data");
        Assertions.assertNotNull(projectData.creationData, "Failed to write creation data");
        Assertions.assertNotNull(projectData.creationData.version, "Failed to write version data");

        //Validate meta
        ObjectMetadata creationData = projectData.creationData;
        Assertions.assertEquals("test.project", creationData.id);
        Assertions.assertEquals(MetaDataLevel.PROJECT, creationData.level);

        //Validate version stuff
        VersionData versionData = creationData.version;
        Assertions.assertEquals("0.0.1.1", versionData.getVersion());
        Assertions.assertEquals("test.project", versionData.getJsonUniqueID());
        Assertions.assertEquals(MetaDataLevel.OBJECT, versionData.getMetaDataLevel());

        //Validate author
        AuthorData authorData = projectData.authorData;
        Assertions.assertEquals("test.project", authorData.getJsonUniqueID());
        Assertions.assertEquals(MetaDataLevel.PROJECT, authorData.getMetaDataLevel());

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

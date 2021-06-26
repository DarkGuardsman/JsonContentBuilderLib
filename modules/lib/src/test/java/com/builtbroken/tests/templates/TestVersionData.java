package com.builtbroken.tests.templates;

import com.builtbroken.builder.handler.IJsonObjectHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.loader.file.FileLocatorSimple;
import com.builtbroken.builder.templates.MetaDataLevel;
import com.builtbroken.builder.templates.VersionData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

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
        IJsonObjectHandler handler = loader.jsonObjectHandlerRegistry.getHandler("builder:version.package");
        Object object = handler.getObject("version.test");

        Assertions.assertTrue(object instanceof VersionData);

        VersionData versionData = (VersionData) object;
        Assertions.assertEquals("0.0.1.1", versionData.getVersion());
        Assertions.assertEquals("version.test", versionData.getJsonUniqueID());
        Assertions.assertEquals(MetaDataLevel.PACKAGE, versionData.getMetaDataLevel());
    }

    @ParameterizedTest(name = "[{index}] input={0} output={1}")
    @MethodSource()
    void testVersionParsing_goodPaths(String versionString, String outputString)
    {
        final VersionData version = new VersionData();
        version.loadVersion(versionString);
        Assertions.assertEquals(outputString, version.getVersion());
    }

    static Stream<Arguments> testVersionParsing_goodPaths()
    {
        return Stream.of(
                Arguments.of("5", "5"),
                Arguments.of("5.5", "5.5"),
                Arguments.of("5.5.5", "5.5.5"),
                Arguments.of("5.5.5", "5.5.5"),
                Arguments.of("5.5.5.5", "5.5.5.5")
        );
    }

    @ParameterizedTest(name = "[{index}] input={0} should throw {1} with message {2}")
    @MethodSource()
    void testVersionParsing_badPaths(String versionString, Class<? extends Throwable> clazz, String message)
    {
        final VersionData version = new VersionData();
        Assertions.assertThrows(clazz, () -> version.loadVersion(versionString), message);
    }

    static Stream<Arguments> testVersionParsing_badPaths()
    {
        return Stream.of(
                Arguments.of(".", IllegalArgumentException.class, "Failed to parse version '.'"),
                Arguments.of("5b", NumberFormatException.class, "Failed to parse version '5b' to get Build Number from '5b'"),
                Arguments.of("5b.0", NumberFormatException.class, "Failed to parse version '5b.0' to get Major from '5b'"),
                Arguments.of("0.5b", NumberFormatException.class, "Failed to parse version '0.5b' to get Minor from '5b'"),
                Arguments.of("0.0.5b", NumberFormatException.class, "Failed to parse version '0.0.5b' to get Revision from '5b'"),
                Arguments.of("0.0.0.5b", NumberFormatException.class, "Failed to parse version '0.0.0.5b' to get Build Number from '5b'"),
                Arguments.of("0.0.0.0.1", IllegalArgumentException.class, "Version can only contain 4 numbers split by '.', instead got 5 values from '0.0.0.0.1")
        );
    }
}

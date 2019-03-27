package com.builtbroken.tests.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import com.builtbroken.builder.io.FileLoaderJar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class TestJarLoading
{

    @Test
    public void testJarPathDetector() throws Exception
    {
        for (String folderPath : TestJsonLoading.TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test/" + folderPath + "/test.jar");

            Assertions.assertTrue(file.exists());
            URL url = new URL("jar:file:/" + file.getAbsolutePath() + "!/content");

            List<String> files = FileLoaderJar.getResourceListing(url);

            Assertions.assertNotNull(files);
            Assertions.assertTrue( !files.isEmpty(), "Failed for: " + folderPath);
            Assertions.assertEquals( "content/test.json", files.get(0),"Failed for: " + folderPath);
        }
    }


    @Test
    public void testJarFilePathLoading() throws Exception
    {
        for (String folderPath : TestJsonLoading.TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.jar");

            Assertions.assertTrue(file.exists(), "No file with path: " + file);

            List<DataFileLoad> files = FileLoaderHandler.loadFile(file);

            TestJsonLoading.validJsonTestFile(files, file.toString());
        }
    }
}

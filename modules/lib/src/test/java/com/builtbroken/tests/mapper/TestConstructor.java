package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class TestConstructor
{

    private static final String TYPE = "test:test.class";

    private static final String TREE = "tree";
    private static final String COUNT = "count";

    private static final String TREE_VALUE = "oak";
    private static final int COUNT_VALUE = 1;


    @Test
    public void testSimple()
    {
        //Data
        final JsonObject jsonData = new JsonObject();
        jsonData.addProperty(TREE, TREE_VALUE);
        jsonData.addProperty(COUNT, COUNT_VALUE);
        jsonData.addProperty("type", TYPE);

        //Setup default loader
        final MainContentLoader loader = new MainContentLoader();
        loader.registerObjectTemplate(TYPE, TYPE, ClassForMappingTest.class, null);
        loader.addFileLocator(() -> Collections.singleton(new DataFileLoad(new File("./fake.json"), jsonData)));
        loader.setup();
        loader.load();


        //Validate we got something
        Assertions.assertEquals(1, loader.generatedObjects.size());

        //Validate we got the expected something
        final Object object = loader.generatedObjects.get(0);
        Assertions.assertTrue(object instanceof GeneratedObject);
        Assertions.assertTrue(((GeneratedObject) object).objectCreated instanceof ClassForMappingTest);

        //Validate the data mapped via the constructor
        final ClassForMappingTest testObject = (ClassForMappingTest) ((GeneratedObject) object).objectCreated;
        Assertions.assertEquals(testObject.testField, TREE_VALUE);
        Assertions.assertEquals(testObject.testField2, COUNT_VALUE);

        //Cleanup
        ContentBuilderLib.destroy();
    }

    public static class ClassForMappingTest implements IJsonGeneratedObject
    {
        public String testField;
        public int testField2;

        @JsonConstructor(type = TYPE)
        public ClassForMappingTest(@JsonMapping(keys = TREE, type = "string") String testField,
                                                 @JsonMapping(keys = COUNT, type = "int") int testField2)
        {
            this. testField = testField;
            this.testField2 = testField2;
        }

        @Override
        public String getJsonTemplateID()
        {
            return TYPE;
        }

        @Override
        public String getJsonUniqueID()
        {
            return testField;
        }
    }

}

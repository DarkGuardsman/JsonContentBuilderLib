package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class TestFieldConstructor
{

    private static final String TYPE = "testClass";
    private static final String TYPE2 = "testClass2";

    private static final String TREE = "tree";
    private static final String COUNT = "count";

    private static final String TREE_VALUE = "oak";
    private static final int COUNT_VALUE = 1;


    @Test
    public void testSupplier()
    {
        //Data
        final JsonObject jsonData = new JsonObject();
        jsonData.addProperty(TREE, TREE_VALUE);
        jsonData.addProperty(COUNT, COUNT_VALUE);
        jsonData.addProperty(ContentBuilderRefs.JSON_TYPE, TYPE);

        //Setup default loader
        final MainContentLoader loader = new MainContentLoader();
        loader.registerObjectTemplate(TYPE, ClassForMappingTest.class, null);
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
    }

    @Test
    public void testFunction()
    {
        //Data
        final JsonObject jsonData = new JsonObject();
        jsonData.addProperty(TREE, TREE_VALUE);
        jsonData.addProperty(COUNT, COUNT_VALUE);
        jsonData.addProperty(ContentBuilderRefs.JSON_TYPE, TYPE2);

        //Setup default loader
        final MainContentLoader loader = new MainContentLoader();
        loader.registerObjectTemplate(TYPE2, ClassForMappingTest2.class, null);
        loader.addFileLocator(() -> Collections.singleton(new DataFileLoad(new File("./fake.json"), jsonData)));
        loader.setup();
        loader.load();

        //Validate we got something
        Assertions.assertEquals(1, loader.generatedObjects.size());

        //Validate we got the expected something
        final Object object = loader.generatedObjects.get(0);
        Assertions.assertTrue(object instanceof GeneratedObject);
        Assertions.assertTrue(((GeneratedObject) object).objectCreated instanceof ClassForMappingTest2);

        //Validate the data mapped via the constructor
        final ClassForMappingTest2 testObject = (ClassForMappingTest2) ((GeneratedObject) object).objectCreated;
        Assertions.assertEquals(testObject.testField, TREE_VALUE);
        Assertions.assertEquals(testObject.testField2, COUNT_VALUE);
    }

    public static class ClassForMappingTest implements IJsonGeneratedObject
    {
        @JsonConstructor
        public static Supplier<ClassForMappingTest> builder = ClassForMappingTest::new;

        @JsonMapping(keys = TREE, type = "string")
        public String testField;

        @JsonMapping(keys = COUNT, type = "int")
        public int testField2;

        @Override
        public String getJsonType()
        {
            return TYPE;
        }

        @Override
        public String getJsonUniqueID()
        {
            return testField;
        }
    }

    public static class ClassForMappingTest2 implements IJsonGeneratedObject
    {
        @JsonConstructor
        public static Function<JsonElement, ClassForMappingTest2> builder = (json) -> new ClassForMappingTest2();

        @JsonMapping(keys = TREE, type = "string")
        public String testField;

        @JsonMapping(keys = COUNT, type = "int")
        public int testField2;

        @Override
        public String getJsonType()
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

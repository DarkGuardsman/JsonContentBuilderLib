package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.oops.GenerateOopMap;

import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class TestMethodConstructor
{

    private static final String TYPE = "testClass";

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

        //map
        final List<Object> objects = ContentBuilderLib.getMainLoader().pipeLine.handle(jsonData, null);

        //Validate we got something
        Assertions.assertEquals(1, objects.size());

        //Validate we got the expected something
        final Object object = objects.get(0);
        Assertions.assertTrue(object instanceof GeneratedObject);
        Assertions.assertTrue(((GeneratedObject) object).objectCreated instanceof ClassForMappingTest);

        //Validate the data mapped via the constructor
        final ClassForMappingTest testObject = (ClassForMappingTest) object;
        Assertions.assertEquals(testObject.testField, TREE_VALUE);
        Assertions.assertEquals(testObject.testField2, COUNT_VALUE);
    }

    @BeforeAll
    public static void setup()
    {
        //Setup
        ContentBuilderLib.getMainLoader().registerObjectTemplate(TYPE, ClassForMappingTest.class, null);
        ContentBuilderLib.getMainLoader().setup();
    }

    @AfterAll
    public static void cleanup()
    {
        //Cleanup
        ContentBuilderLib.destroy();
    }

    private static class ClassForMappingTest implements IJsonGeneratedObject
    {
        public String testField;
        public String testField2;

        @JsonConstructor(type = TYPE)
        public static ClassForMappingTest newObj(@JsonMapping(keys = TREE) String testField,
                                                 @JsonMapping(keys = COUNT) String testField2)
        {
            ClassForMappingTest classForMappingTest = new ClassForMappingTest();
            classForMappingTest.testField = testField;
            classForMappingTest.testField2 = testField2;
            return classForMappingTest;
        }

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

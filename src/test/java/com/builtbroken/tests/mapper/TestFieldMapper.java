package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.mapper.JsonMapping;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.builtbroken.tests.UnitTestHelpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class TestFieldMapper
{
    @Test
    public void testSimple()
    {
        //Data
        JsonObject json = new JsonObject();
        json.addProperty("test", "trees");
        json.addProperty("test_byte", (byte)3);
        json.addProperty("test_short", (short)45);
        json.addProperty("test_int", 5);
        json.addProperty("test_long", 15L);
        json.addProperty("test_float", 2.3f);
        json.addProperty("test_double", 45.9);

        json.add("test_array", UnitTestHelpers.createJsonStringArray("1", "hj", "klj"));

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, json, false);

        Assertions.assertEquals(object.testField, "trees");
        Assertions.assertEquals(object.testByteField, (byte)3);
        Assertions.assertEquals(object.testShortField, (short)45);
        Assertions.assertEquals(object.testIntField, 5);
        Assertions.assertEquals(object.testLongField, 15L);
        Assertions.assertEquals(object.testFloatField, 2.3f);
        Assertions.assertEquals(object.testDoubleField, 45.9);

        Assertions.assertNotNull(object.testArrayField);
        Assertions.assertEquals(3, object.testArrayField.length);
        Assertions.assertEquals("1", object.testArrayField[0]);
        Assertions.assertEquals("hj", object.testArrayField[1]);
        Assertions.assertEquals("klj", object.testArrayField[2]);
    }

    @BeforeAll
    public static void setup()
    {
        //Setup
        ContentBuilderLib.getMainLoader().load();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(ClassForMappingTest.class, "testClass");
    }

    @AfterAll
    public static void cleanup()
    {
        //Cleanup
        ContentBuilderLib.destroy();
    }

    private static class ClassForMappingTest
    {
        @JsonMapping(keys = "test", type = "string")
        public String testField;

        @JsonMapping(keys = "test_array", type = "java:array.string")
        public String[] testArrayField;

        @JsonMapping(keys = "test_byte", type = "byte")
        public byte testByteField;

        @JsonMapping(keys = "test_short", type = "short")
        public short testShortField;

        @JsonMapping(keys = "test_int", type = "int")
        public int testIntField;

        @JsonMapping(keys = "test_long", type = "long")
        public long testLongField;

        @JsonMapping(keys = "test_float", type = "float")
        public float testFloatField;

        @JsonMapping(keys = "test_double", type = "double")
        public double testDoubleField;
    }

}

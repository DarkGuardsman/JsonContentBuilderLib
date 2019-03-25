package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.mapper.JsonMapping;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class TestFieldMapper extends TestCase
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

        JsonArray array = new JsonArray();
        array.add("1");
        array.add("hj");
        array.add("klj");
        json.add("test_array", array);

        //Trigger setup
        JsonMappingHandler.register(ClassForMappingTest.class, "testClass");
        ContentBuilderLib.setup();

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        JsonMappingHandler.map("testClass", object, json, ContentBuilderLib.getMainLoader(), false);

        assertEquals(object.testField, "trees");
        assertEquals(object.testByteField, (byte)3);
        assertEquals(object.testShortField, (short)45);
        assertEquals(object.testIntField, 5);
        assertEquals(object.testLongField, 15L);
        assertEquals(object.testFloatField, 2.3f);
        assertEquals(object.testDoubleField, 45.9);

        assertNotNull(object.testArrayField);
        assertEquals(3, object.testArrayField.length);
        assertEquals("1", object.testArrayField[0]);
        assertEquals("hj", object.testArrayField[1]);
        assertEquals("klj", object.testArrayField[2]);
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

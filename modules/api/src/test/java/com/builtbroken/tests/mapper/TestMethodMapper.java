package com.builtbroken.tests.mapper;

import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-25.
 */
public class TestMethodMapper
{

    @Test
    public void testSimple()
    {
        //Data
        JsonObject json = new JsonObject();
        json.addProperty("test", "trees");
        json.addProperty("test_byte", (byte) 3);
        json.addProperty("test_short", (short) 45);
        json.addProperty("test_int", 5);
        json.addProperty("test_long", 15L);
        json.addProperty("test_float", 2.3f);
        json.addProperty("test_double", 45.9);

        JsonArray array = new JsonArray();
        array.add("1");
        array.add("hj");
        array.add("klj");
        json.add("test_array", array);

        //Setup
        final MainContentLoader loader = new MainContentLoader();
        loader.setup();
        loader.jsonMappingHandler.register(ClassForMappingTest.class, "testClass");

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        loader.jsonMappingHandler.map("testClass", object, json, false);

        Assertions.assertEquals(object.testField, "trees");
        Assertions.assertEquals(object.testByteField, (byte) 3);
        Assertions.assertEquals(object.testShortField, (short) 45);
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

    private static class ClassForMappingTest
    {

        public String testField;
        public String[] testArrayField;
        public byte testByteField;
        public short testShortField;
        public int testIntField;
        public long testLongField;
        public float testFloatField;
        public double testDoubleField;

        @JsonMapping(keys = "test", type = "string")
        public void method1(String testField)
        {
            this.testField = testField;
        }

        @JsonMapping(keys = "test_array", type = "java:array.string")
        public void method2(String[] testArrayField)
        {
            this.testArrayField = testArrayField;
        }

        @JsonMapping(keys = "test_byte", type = "byte")
        public void method3(byte testByteField)
        {
            this.testByteField = testByteField;
        }

        @JsonMapping(keys = "test_short", type = "short")
        public void method4(short testShortField)
        {
            this.testShortField = testShortField;
        }

        @JsonMapping(keys = "test_int", type = "int")
        public void method5(int testIntField)
        {
            this.testIntField = testIntField;
        }

        @JsonMapping(keys = "test_long", type = "long")
        public void method6(long testLongField)
        {
            this.testLongField = testLongField;
        }

        @JsonMapping(keys = "test_float", type = "float")
        public void method7(float testFloatField)
        {
            this.testFloatField = testFloatField;
        }

        @JsonMapping(keys = "test_double", type = "double")
        public void method8(double testDoubleField)
        {
            this.testDoubleField = testDoubleField;
        }
    }

}

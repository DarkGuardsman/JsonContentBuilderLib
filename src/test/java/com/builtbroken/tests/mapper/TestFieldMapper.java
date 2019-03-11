package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.mapper.JsonMapping;
import com.builtbroken.builder.mapper.JsonMappingHandler;
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
        json.addProperty("test_int", 5);

        //Trigger setup
        JsonMappingHandler.register(ClassForMappingTest.class, "testClass");
        ContentBuilderLib.setup();

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        JsonMappingHandler.map("testClass", object, json, ContentBuilderLib.MAIN_CONVERTER);

        assertEquals(object.testField, "trees");
        assertEquals(object.testByteField, (byte)3);
        assertEquals(object.testIntField, 5);
    }

    private static class ClassForMappingTest
    {
        @JsonMapping(keys = "test", type = "string")
        public String testField;

        @JsonMapping(keys = "test_byte", type = "byte")
        public byte testByteField;

        @JsonMapping(keys = "test_int", type = "int")
        public int testIntField;
    }

}

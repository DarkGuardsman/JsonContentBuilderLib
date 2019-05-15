package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayByte;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestFieldEnum
{

    @Test
    public void testEnumString()
    {
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("test", "a");

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, jsonData, false);

        //Test
        Assertions.assertEquals(TestEnum.A, object.testEnum);
    }

    @Test
    public void testEnumNumber()
    {
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("test", 1);

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, jsonData, false);

        //Test
        Assertions.assertEquals(TestEnum.B, object.testEnum);
    }

    @BeforeAll
    public static void setup()
    {
        //Setup
        ContentBuilderLib.getMainLoader().setup();
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

        @JsonMapping(keys = "test", type = ConverterRefs.ENUM)
        public TestEnum testEnum;
    }

    public static enum TestEnum
    {
        A,
        B
    }
}

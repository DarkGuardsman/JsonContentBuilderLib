package com.builtbroken.tests.mapper;

import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestMethodEnum
{

    @Test
    public void testEnumString()
    {
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("test", "a");

        //Setup
        final ContentLoader loader = new MainContentLoader();
        loader.setup();
        loader.jsonMappingHandler.register(ClassForMappingTest.class, "testClass");

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        loader.jsonMappingHandler.map("testClass", object, jsonData, false);

        //Test
        Assertions.assertEquals(TestEnum.A, object.testEnum);
    }

    @Test
    public void testEnumNumber()
    {
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("test", 1);

        //Setup
        final ContentLoader loader = new MainContentLoader();
        loader.setup();
        loader.jsonMappingHandler.register(ClassForMappingTest.class, "testClass");

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        loader.jsonMappingHandler.map("testClass", object, jsonData, false);

        //Test
        Assertions.assertEquals(TestEnum.B, object.testEnum);
    }

    private static class ClassForMappingTest
    {
        public TestEnum testEnum;

        @JsonMapping(keys = "test", type = ConverterRefs.ENUM)
        public void method(TestEnum testEnum)
        {
            this.testEnum = testEnum;
        }
    }

    public static enum TestEnum
    {
        A,
        B
    }
}

package com.builtbroken.builder.mapper;

import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        //Setup default loader
        final MainContentLoader loader = new MainContentLoader();
        loader.jsonMappingHandler.register(ClassForMappingTest.class, "testClass");
        loader.setup();

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

        //Setup default loader
        final MainContentLoader loader = new MainContentLoader();
        loader.jsonMappingHandler.register(ClassForMappingTest.class, "testClass");
        loader.setup();

        //map
        ClassForMappingTest object = new ClassForMappingTest();
        loader.jsonMappingHandler.map("testClass", object, jsonData, false);

        //Test
        Assertions.assertEquals(TestEnum.B, object.testEnum);
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

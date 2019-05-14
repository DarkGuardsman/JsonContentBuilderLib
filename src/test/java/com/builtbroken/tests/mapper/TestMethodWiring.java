package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class TestMethodWiring
{

    protected static final String OBJECT_ID = "trees";
    protected static final String TYPE_ID = "test";
    protected static final String MAP_ID = "testClass";
    protected static final String JSON_ID = "test_id";

    @Test
    public void testSimple()
    {
        //Data
        JsonObject json = new JsonObject();
        json.addProperty(JSON_ID, OBJECT_ID);

        //Add object
        final Object clazzToWire = new ClassToWire();
        ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.onCreated(new GeneratedObject(TYPE_ID, clazzToWire, null));

        //Map
        ClassForLinkTest object = new ClassForLinkTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map(MAP_ID, object, json, true);

        //Test
        Assertions.assertEquals(clazzToWire, object.testField);


    }

    @BeforeAll
    public static void setup()
    {
        //Setup
        ContentBuilderLib.getMainLoader().setup();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(ClassForLinkTest.class, MAP_ID);
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(ClassToWire.class, TYPE_ID);
        ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.createOrGetHandler(TYPE_ID);
    }

    @AfterAll
    public static void cleanup()
    {
        //Cleanup
        ContentBuilderLib.destroy();
    }

    private static class ClassToWire implements IJsonGeneratedObject
    {

        @Override
        public String getJsonType()
        {
            return TYPE_ID;
        }

        @Override
        public String getJsonUniqueID()
        {
            return OBJECT_ID;
        }
    }

    private static class ClassForLinkTest
    {
        public ClassToWire testField;

        @JsonObjectWiring(jsonFields = JSON_ID, objectType = TYPE_ID)
        public void mapToMe(ClassToWire abc)
        {
            this.testField = abc;
        }
    }

}

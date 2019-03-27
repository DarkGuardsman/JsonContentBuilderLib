package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.builtbroken.builder.mapper.JsonObjectWiring;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public class TestFieldWiring extends TestCase
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

        //Setup
        ContentBuilderLib.setup();
        JsonMappingHandler.register(ClassForLinkTest.class, MAP_ID);
        JsonMappingHandler.register(ClassToWire.class, TYPE_ID);
        ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.createOrGetHandler(TYPE_ID);

        //Add object
        final Object clazzToWire = new ClassToWire();
        ContentBuilderLib.getMainLoader().jsonObjectHandlerRegistry.onCreated(new GeneratedObject(TYPE_ID, clazzToWire, null));

        //Map
        ClassForLinkTest object = new ClassForLinkTest();
        JsonMappingHandler.map(MAP_ID, object, json, ContentBuilderLib.getMainLoader(), true);

        //Test
        assertEquals(clazzToWire, object.testField);

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
        @JsonObjectWiring(jsonFields = JSON_ID, objectType = TYPE_ID)
        public ClassToWire testField;
    }

}
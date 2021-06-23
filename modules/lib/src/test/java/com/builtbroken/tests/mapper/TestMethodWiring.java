package com.builtbroken.tests.mapper;

import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.loader.MainContentLoader;
import com.builtbroken.builder.mapper.anno.JsonObjectWiring;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
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
        //Setup
        final ContentLoader loader = new MainContentLoader();
        loader.setup();
        loader.jsonMappingHandler.register(ClassForLinkTest.class, MAP_ID);
        loader.jsonMappingHandler.register(ClassToWire.class, TYPE_ID);
        loader.jsonObjectHandlerRegistry.createOrGetHandler(TYPE_ID);

        //Data
        JsonObject json = new JsonObject();
        json.addProperty(JSON_ID, OBJECT_ID);

        //Add object
        final Object clazzToWire = new ClassToWire();
        loader.jsonObjectHandlerRegistry.onCreated(new GeneratedObject(TYPE_ID, clazzToWire, null));

        //Map
        ClassForLinkTest object = new ClassForLinkTest();
        loader.jsonMappingHandler.map(MAP_ID, object, json, true);

        //Test
        Assertions.assertEquals(clazzToWire, object.testField);


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

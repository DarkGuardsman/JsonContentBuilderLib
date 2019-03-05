package com.builtbroken.tests.pipe.building;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.JsonConverter;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.pipe.nodes.building.PipeNodeObjectCreator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestObjectCreator extends TestCase
{
    @Test
    public void testBuild()
    {
        //Setup handler
        final ConversionHandler handler = new ConversionHandler(null,"test");
        handler.addConverter(new MockConverter());

        //Create builder, override get for converter so we can make this test standalone
        PipeNodeObjectCreator pipeNodeObjectCreator = new PipeNodeObjectCreator(null)
        {
            @Override
            protected ConversionHandler getConverter()
            {
                return handler;
            }
        };

        //Generate test object
        JsonObject object = new JsonObject();
        object.addProperty("type", "mock");

        //Build
        LinkedList<Object> out = new LinkedList();
        pipeNodeObjectCreator.receive(object, object, out);

        assertEquals(1, out.size());

        assertTrue(out.get(0) instanceof GeneratedObject);
        assertEquals(((GeneratedObject)out.get(0)).type, "mock");
        assertEquals(((GeneratedObject)out.get(0)).jsonUsed, object);
        assertTrue(((GeneratedObject)out.get(0)).objectCreated instanceof MockObject);
    }

    private static class MockObject
    {

    }

    private static class MockConverter extends JsonConverter
    {
        public MockConverter()
        {
            super("mock");
        }

        @Override
        public boolean canSupport(Object object)
        {
            return true;
        }

        @Override
        public boolean canSupport(JsonElement json)
        {
            return true;
        }

        @Override
        public JsonElement toJson(Object object)
        {
            return null;
        }

        @Override
        public Object fromJson(JsonElement element)
        {
            return new MockObject();
        }
    }
}

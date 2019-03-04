package com.builtbroken.tests.pipe.cleaning;

import com.builtbroken.builder.pipe.nodes.json.PipeNodeJsonSplitter;
import com.builtbroken.tests.UnitTestHelpers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-04.
 */
public class TestJsonSplitter extends TestCase
{
    @Test
    public void testFile()
    {
        JsonElement json = UnitTestHelpers.getJsonFromTestFolder("test_split.json");
        PipeNodeJsonSplitter splitter = new PipeNodeJsonSplitter();

        final LinkedList<Object> out = new LinkedList();
        splitter.receive(json, json, out);

        assertEquals(6, out.size());

        for(int i = 0; i < 6; i++)
        {
            Object object = out.get(i);
            assertTrue(object instanceof JsonObject);

            JsonObject jsonObject = ((JsonObject)object);
            assertTrue(jsonObject.size() > 0);
            assertTrue(jsonObject.has("type"));
            assertTrue(jsonObject.has("data"));
            //TODO test exact layout to be 100% sure we didn't break anything
        }
    }
}

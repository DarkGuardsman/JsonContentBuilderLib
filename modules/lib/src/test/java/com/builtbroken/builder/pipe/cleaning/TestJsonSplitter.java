package com.builtbroken.builder.pipe.cleaning;

import com.builtbroken.builder.pipe.nodes.json.PipeNodeJsonSplitter;
import com.builtbroken.builder.UnitTestHelpers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-04.
 */
public class TestJsonSplitter
{

    @Test
    public void testFile()
    {
        JsonElement json = UnitTestHelpers.getJsonFromTestFolder("test_split.json");
        PipeNodeJsonSplitter splitter = new PipeNodeJsonSplitter();

        final LinkedList<Object> out = new LinkedList();
        splitter.receive(json, json.getAsJsonArray(), out);

        Assertions.assertEquals(6, out.size());

        for (int i = 0; i < 6; i++)
        {
            Object object = out.get(i);
            Assertions.assertTrue(object instanceof JsonObject);

            JsonObject jsonObject = ((JsonObject) object);
            Assertions.assertTrue(jsonObject.size() > 0);
            Assertions.assertTrue(jsonObject.has("type"));
            Assertions.assertTrue(jsonObject.has("data"));
            //TODO test exact layout to be 100% sure we didn't break anything
        }
    }
}

package com.builtbroken.tests.pipe;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.pipe.PipeLine;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class TestPipeLine
{
    @Test
    public void testPipeFlowJson()
    {
        PipeLine pipeLine = PipeLine.newDefault();

        //Build array for testing
        JsonArray array = new JsonArray();

        JsonObject object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "tree");
        array.add(object);


        object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "log");
        array.add(object);


        object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "stick");
        array.add(object);


        //Only run json pipe
        List<Object> out = pipeLine.handle(array, pipe -> pipe.pipeName != ContentBuilderRefs.PIPE_JSON);
        Assertions.assertNotNull(out);
        Assertions.assertEquals(3, out.size());
        testPipeFlowJsonType(out.get(0), "tree");
        testPipeFlowJsonType(out.get(1), "log");
        testPipeFlowJsonType(out.get(2), "stick");
    }

    private void testPipeFlowJsonType(Object object, String type)
    {
        Assertions.assertTrue(object instanceof JsonObject);
        Assertions.assertTrue(((JsonObject)object).has("type"));
        Assertions.assertEquals(((JsonObject)object).getAsJsonPrimitive("type").getAsString(), type);
        Assertions.assertFalse(((JsonObject)object).has("_Comment"));
    }
}

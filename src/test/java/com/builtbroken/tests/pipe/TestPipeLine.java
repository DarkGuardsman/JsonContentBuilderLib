package com.builtbroken.tests.pipe;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.JsonMapping;
import com.builtbroken.builder.mapper.JsonObjectWiring;
import com.builtbroken.builder.pipe.PipeLine;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;

import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class TestPipeLine
{

    @BeforeAll
    public static void setup()
    {
        ContentBuilderLib.getMainLoader().setup();
        ContentBuilderLib.getMainLoader().registerObject("tree", TreeTest.class, (json) -> new TreeTest());
        ContentBuilderLib.getMainLoader().registerObject("log", LogTest.class, (json) -> new LogTest());
        ContentBuilderLib.getMainLoader().registerObject("stick", StickTest.class, (json) -> new StickTest());
    }

    @AfterAll
    public static void cleanup()
    {
        ContentBuilderLib.destroy();
    }

    @Test
    public void testPipeFlowJson()
    {
        final PipeLine pipeLine = ContentBuilderLib.getMainLoader().pipeLine;

        //Build array for testing
        final JsonArray array = generateJson();


        //Only run json pipe
        List<Object> out = pipeLine.handle(array, pipe -> pipe.pipeName != ContentBuilderRefs.PIPE_JSON);
        Assertions.assertNotNull(out);
        Assertions.assertEquals(3, out.size());
        testPipeFlowJsonType(out.get(0), "tree", "log3", 0);
        testPipeFlowJsonType(out.get(1), "log", "stick5", 1);
        testPipeFlowJsonType(out.get(2), "stick", null, 2);
    }

    @Test
    public void testPipeFlowBuild()
    {
        final PipeLine pipeLine = ContentBuilderLib.getMainLoader().pipeLine;

        //Build array for testing
        final JsonArray array = generateJson();


        //Only run json pipe
        List<Object> out = pipeLine.handle(array, pipe -> pipe.pipeName == ContentBuilderRefs.PIPE_MAPPER);
        Assertions.assertNotNull(out);
        Assertions.assertEquals(3, out.size());
        Assertions.assertTrue(out.get(0) instanceof GeneratedObject);
        Assertions.assertTrue(((GeneratedObject) out.get(0)).objectCreated instanceof TreeTest);

        Assertions.assertTrue(out.get(1) instanceof GeneratedObject);
        Assertions.assertTrue(((GeneratedObject) out.get(1)).objectCreated instanceof LogTest);

        Assertions.assertTrue(out.get(2) instanceof GeneratedObject);
        Assertions.assertTrue(((GeneratedObject) out.get(2)).objectCreated instanceof StickTest);

    }

    @Test
    public void testPipeFlowMapping()
    {
        final PipeLine pipeLine = ContentBuilderLib.getMainLoader().pipeLine;

        //Build array for testing
        final JsonArray array = generateJson();


        //Only run json pipe
        List<Object> out = pipeLine.handle(array, null);
        Assertions.assertNotNull(out);
        Assertions.assertEquals(3, out.size());
        TreeTest tree = (TreeTest) ((GeneratedObject) out.get(0)).objectCreated;
        LogTest log = (LogTest) ((GeneratedObject) out.get(1)).objectCreated;
        StickTest stick = (StickTest) ((GeneratedObject) out.get(2)).objectCreated;

        Assertions.assertEquals(0, tree.i);
        Assertions.assertEquals(1, log.i);
        Assertions.assertEquals(2, stick.i);

        Assertions.assertEquals(log, tree.log);
        Assertions.assertEquals(stick, log.stick);

    }

    private JsonArray generateJson()
    {
        JsonArray array = new JsonArray();

        JsonObject object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "tree");
        object.addProperty("child", "log3");
        object.addProperty("i", 0);
        array.add(object);


        object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "log");
        object.addProperty("child", "stick5");
        object.addProperty("i", 1);
        array.add(object);


        object = new JsonObject();
        object.addProperty("_Comment", "Something something tests");
        object.addProperty("type", "stick");
        object.addProperty("i", 2);
        array.add(object);

        return array;
    }

    private void testPipeFlowJsonType(Object object, String type, String child, int id)
    {
        Assertions.assertTrue(object instanceof JsonObject);

        Assertions.assertTrue(((JsonObject) object).has("type"));
        Assertions.assertEquals(((JsonObject) object).getAsJsonPrimitive("type").getAsString(), type);

        Assertions.assertFalse(((JsonObject) object).has("_Comment"));

        Assertions.assertTrue(((JsonObject) object).has("i"));
        Assertions.assertEquals(id, ((JsonObject) object).get("i").getAsJsonPrimitive().getAsInt());

        if (child != null)
        {
            Assertions.assertTrue(((JsonObject) object).has("child"));
            Assertions.assertEquals(child, ((JsonObject) object).get("child").getAsJsonPrimitive().getAsString());
        }
    }

    public static class TreeTest implements IJsonGeneratedObject
    {

        @JsonMapping(keys = "i", type = "int")
        public int i;

        @JsonObjectWiring(jsonFields = "child", objectType = "log")
        public LogTest log;

        @Override
        public String getJsonType()
        {
            return "tree";
        }

        @Override
        public String getJsonUniqueID()
        {
            return "tree2";
        }
    }

    public static class LogTest implements IJsonGeneratedObject
    {

        @JsonMapping(keys = "i", type = "int")
        public int i;

        @JsonObjectWiring(jsonFields = "child", objectType = "stick")
        public StickTest stick;

        @Override
        public String getJsonType()
        {
            return "log";
        }

        @Override
        public String getJsonUniqueID()
        {
            return "log3";
        }
    }

    public static class StickTest implements IJsonGeneratedObject
    {

        @JsonMapping(keys = "i", type = "int")
        public int i;

        @Override
        public String getJsonType()
        {
            return "stick";
        }

        @Override
        public String getJsonUniqueID()
        {
            return "stick5";
        }
    }
}

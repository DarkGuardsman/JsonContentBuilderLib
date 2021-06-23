package com.builtbroken.tests.pipe.cleaning;

import com.builtbroken.builder.pipe.nodes.json.JsonHelpers;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeCommentRemover;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class TestCommentCleaner
{

    @Test
    public void testEmpty()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover(null);
        final JsonObject generatedJson = new JsonObject();

        //Run
        Queue<Object> queue = new LinkedList();
        final JsonObject insert = JsonHelpers.deepCopy(generatedJson);
        commentRemover.receive(insert, insert, queue);

        //Check that we added JSON to output
        Assertions.assertSame(queue.peek(), insert);

        //Check that json is empty
        Assertions.assertEquals(0, insert.size(), "Comment remover should have done nothing " + generatedJson.toString() + "  " + insert.toString());
    }

    @Test
    public void testBasic()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover(null);

        for (int i = 0; i < 10; i++)
        {
            //Setup test object
            final JsonObject generatedJson = new JsonObject();
            for (int j = 0; j <= i; j++)
            {
                generatedJson.addProperty("_comment:" + j, "");
            }
            Assertions.assertEquals(i + 1, generatedJson.size(), "Test failed to generate JSON with " + (i + i) + " comments");

            //Run
            Queue<Object> queue = new LinkedList();
            final JsonObject insert = JsonHelpers.deepCopy(generatedJson);
            commentRemover.receive(insert, insert, queue);

            //Check that we added JSON to output
            Assertions.assertSame(queue.peek(), insert);

            //Check that json is empty
            Assertions.assertEquals(0, insert.size(), "Comment remover failed to empty object " + generatedJson.toString());
        }
    }

    @Test
    public void testBasicWithRandom()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover(null);
        Random random = new Random();

        for (int timesToRun = 0; timesToRun < 10; timesToRun++)
        {
            //Setup test object
            final JsonObject generatedJson = new JsonObject();

            int commentsAdded = 0;
            for (int j = 0; j < 20; j++)
            {
                if (random.nextBoolean())
                {
                    commentsAdded++;
                    generatedJson.addProperty("_comment:" + j, "" + random.nextInt());
                }
                else
                {
                    generatedJson.addProperty("data:" + j, "" + random.nextInt());
                }
            }
            Assertions.assertEquals(20, generatedJson.size(), "Test failed to generate JSON with 20 entries");

            //Run
            Queue<Object> queue = new LinkedList();
            final JsonObject insert = JsonHelpers.deepCopy(generatedJson);
            commentRemover.receive(insert, insert, queue);

            //Check that we added JSON to output
            Assertions.assertSame(queue.peek(), insert);

            //Check that json is empty
            Assertions.assertEquals(20 - commentsAdded, insert.size(), "Comment remover failed to empty object " + generatedJson.toString());

            for (Map.Entry<String, JsonElement> entry : insert.entrySet())
            {
                if (entry.getKey().startsWith("_"))
                {
                    Assertions.fail("Comment remover failed to remover comments from " + insert);
                }
                else
                {
                    Assertions.assertEquals(generatedJson.get(entry.getKey()), insert.get(entry.getKey()), "Comment remover edited non-comment data" + generatedJson.toString() + "  " + insert.toString());
                }
            }
        }
    }

    @Test
    public void testArray()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover(null);
        final JsonObject generatedJson = new JsonObject();
        JsonArray array = new JsonArray();
        array.add("1");
        array.add("2");
        array.add("43");
        array.add("23");
        array.add("24");
        array.add("267");
        array.add("2213");
        generatedJson.add("array", array);

        //Run
        Queue<Object> queue = new LinkedList();
        final JsonObject insert = JsonHelpers.deepCopy(generatedJson);
        commentRemover.receive(insert, insert, queue);

        //Check that we added JSON to output
        Assertions.assertSame(queue.peek(), insert);

        //Check that json is empty
        Assertions.assertEquals(1, insert.size(), "Comment remover should have done nothing " + generatedJson.toString() + "  " + insert.toString());
        Assertions.assertNotNull(insert.get("array"));
        Assertions.assertTrue(insert.get("array").isJsonArray());
        Assertions.assertEquals(7, insert.get("array").getAsJsonArray().size());
    }

    @Test
    public void testObject()
    {

    }

    @Test
    public void testNestedObject()
    {

    }

    private JsonObject fromString(String s)
    {
        JsonParser parser = new JsonParser();
        return parser.parse(s).getAsJsonObject();
    }
}

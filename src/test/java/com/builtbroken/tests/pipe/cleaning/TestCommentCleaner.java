package com.builtbroken.tests.pipe.cleaning;

import com.builtbroken.builder.pipe.nodes.cleaning.PipeNodeCommentRemover;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class TestCommentCleaner extends TestCase
{

    @Test
    public void testEmpty()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover();
        final JsonObject generatedJson = new JsonObject();

        //Run
        Queue<Object> queue = new LinkedList();
        final JsonObject insert = generatedJson.deepCopy();
        commentRemover.receive(insert, null, queue);

        //Check that we added JSON to output
        assertSame(queue.peek(), insert);

        //Check that json is empty
        assertEquals("Comment remover should have done nothing " + generatedJson.toString() + "  " + insert.toString(), 0, insert.size());
    }

    @Test
    public void testBasic()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover();

        for (int i = 0; i < 10; i++)
        {
            //Setup test object
            final JsonObject generatedJson = new JsonObject();
            for (int j = 0; j <= i; j++)
            {
                generatedJson.addProperty("_comment:" + j, "");
            }
            assertEquals("Test failed to generate JSON with " + (i + i) + " comments", i + 1, generatedJson.size());

            //Run
            Queue<Object> queue = new LinkedList();
            final JsonObject insert = generatedJson.deepCopy();
            commentRemover.receive(insert, null, queue);

            //Check that we added JSON to output
            assertSame(queue.peek(), insert);

            //Check that json is empty
            assertEquals("Comment remover failed to empty object " + generatedJson.toString(), 0, insert.size());
        }
    }

    @Test
    public void testBasicWithRandom()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover();
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
            assertEquals("Test failed to generate JSON with 20 entries", 20, generatedJson.size());

            //Run
            Queue<Object> queue = new LinkedList();
            final JsonObject insert = generatedJson.deepCopy();
            commentRemover.receive(insert, null, queue);

            //Check that we added JSON to output
            assertSame(queue.peek(), insert);

            //Check that json is empty
            assertEquals("Comment remover failed to empty object " + generatedJson.toString(), 20 - commentsAdded, insert.size());

            for (String s : insert.keySet())
            {
                if (s.startsWith("_"))
                {
                    fail("Comment remover failed to remover comments from " + insert);
                }
                else
                {
                    assertEquals("Comment remover edited non-comment data" + generatedJson.toString() + "  " + insert.toString(), generatedJson.get(s), insert.get(s));
                }
            }
        }
    }

    @Test
    public void testArray()
    {
        PipeNodeCommentRemover commentRemover = new PipeNodeCommentRemover();
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
        final JsonObject insert = generatedJson.deepCopy();
        commentRemover.receive(insert, null, queue);

        //Check that we added JSON to output
        assertSame(queue.peek(), insert);

        //Check that json is empty
        assertEquals("Comment remover should have done nothing " + generatedJson.toString() + "  " + insert.toString(), 1, insert.size());
        assertNotNull(insert.get("array"));
        assertTrue(insert.get("array").isJsonArray());
        assertEquals(7, insert.get("array").getAsJsonArray().size());
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

package com.builtbroken.tests.pipe.cleaning;

import com.builtbroken.builder.pipe.nodes.cleaning.PipeNodeCommentRemover;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/26/19.
 */
public class TestCommentCleaner extends TestCase
{

    @Test
    public void testCleanerBasic()
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


    private JsonObject fromString(String s)
    {
        JsonParser parser = new JsonParser();
        return parser.parse(s).getAsJsonObject();
    }
}

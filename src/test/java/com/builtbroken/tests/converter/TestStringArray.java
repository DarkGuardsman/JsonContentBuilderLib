package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayByte;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestStringArray extends TestCase
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayString converter = new JsonConverterArrayString();
        String[] array = new String[]{"a", "b", "e", "1"};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals("a", element.getAsJsonArray().get(0).getAsString());
        assertEquals("b", element.getAsJsonArray().get(1).getAsString());
        assertEquals("e", element.getAsJsonArray().get(2).getAsString());
        assertEquals("1", element.getAsJsonArray().get(3).getAsString());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayString converter = new JsonConverterArrayString();
        JsonArray element = new JsonArray();
        element.add("g");
        element.add("a");
        element.add("f");
        element.add("1");

        Object array = converter.fromJson(element, null);
        assertTrue(array instanceof String[]);
        assertEquals("g", ((String[])array)[0]);
        assertEquals("a", ((String[])array)[1]);
        assertEquals("f", ((String[])array)[2]);
        assertEquals("1", ((String[])array)[3]);
    }
}

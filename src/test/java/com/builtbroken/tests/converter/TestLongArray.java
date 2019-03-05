package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayInt;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayLong;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestLongArray extends TestCase
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayLong converter = new JsonConverterArrayLong();
        long[] array = new long[]{2L, 5L, 9L, 1L};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals(2L, element.getAsJsonArray().get(0).getAsLong());
        assertEquals(5L, element.getAsJsonArray().get(1).getAsLong());
        assertEquals(9L, element.getAsJsonArray().get(2).getAsLong());
        assertEquals(1L, element.getAsJsonArray().get(3).getAsLong());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayLong converter = new JsonConverterArrayLong();
        JsonArray element = new JsonArray();
        element.add(3L);
        element.add(5L);
        element.add(6L);
        element.add(7L);

        Object array = converter.fromJson(element, null);
        assertTrue(array instanceof long[]);
        assertEquals(3L, ((long[])array)[0]);
        assertEquals(5L, ((long[])array)[1]);
        assertEquals(6L, ((long[])array)[2]);
        assertEquals(7L, ((long[])array)[3]);
    }
}

package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayInt;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestArrayConverters extends TestCase
{

    @Test
    public void testIntArrayToJson()
    {
        JsonConverterArrayInt converter = new JsonConverterArrayInt();
        int[] array = new int[]{2, 5, 9, 1};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals(2, element.getAsJsonArray().get(0).getAsInt());
        assertEquals(5, element.getAsJsonArray().get(1).getAsInt());
        assertEquals(9, element.getAsJsonArray().get(2).getAsInt());
        assertEquals(1, element.getAsJsonArray().get(3).getAsInt());
    }

    @Test
    public void testIntArrayFromJson()
    {
        JsonConverterArrayInt converter = new JsonConverterArrayInt();
        JsonArray element = new JsonArray();
        element.add(3);
        element.add(5);
        element.add(6);
        element.add(7);

        Object array = converter.fromJson(element, null);
        assertTrue(array instanceof int[]);
        assertEquals(3, ((int[])array)[0]);
        assertEquals(5, ((int[])array)[1]);
        assertEquals(6, ((int[])array)[2]);
        assertEquals(7, ((int[])array)[3]);
    }
}

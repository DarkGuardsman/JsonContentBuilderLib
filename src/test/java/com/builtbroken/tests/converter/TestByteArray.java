package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayByte;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayInt;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestByteArray extends TestCase
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayByte converter = new JsonConverterArrayByte();
        byte[] array = new byte[]{2, 5, 9, 1};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals((byte) 2, element.getAsJsonArray().get(0).getAsByte());
        assertEquals((byte) 5, element.getAsJsonArray().get(1).getAsByte());
        assertEquals((byte) 9, element.getAsJsonArray().get(2).getAsByte());
        assertEquals((byte) 1, element.getAsJsonArray().get(3).getAsByte());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayByte converter = new JsonConverterArrayByte();
        JsonArray element = new JsonArray();
        element.add(3);
        element.add(5);
        element.add(6);
        element.add(7);

        Object array = converter.fromJson(element, null);
        assertTrue(array instanceof byte[]);
        assertEquals((byte) 3, ((byte[]) array)[0]);
        assertEquals((byte) 5, ((byte[]) array)[1]);
        assertEquals((byte) 6, ((byte[]) array)[2]);
        assertEquals((byte) 7, ((byte[]) array)[3]);
    }
}

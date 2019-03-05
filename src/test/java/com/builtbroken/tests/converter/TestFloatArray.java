package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayFloat;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayShort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestFloatArray extends TestCase
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayFloat converter = new JsonConverterArrayFloat();
        float[] array = new float[]{2f, 5f, 9f, 1f};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals(2f, element.getAsJsonArray().get(0).getAsFloat());
        assertEquals(5f, element.getAsJsonArray().get(1).getAsFloat());
        assertEquals(9f, element.getAsJsonArray().get(2).getAsFloat());
        assertEquals(1f, element.getAsJsonArray().get(3).getAsFloat());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayFloat converter= new JsonConverterArrayFloat();
        JsonArray element = new JsonArray();
        element.add(3L);
        element.add(5L);
        element.add(6L);
        element.add(7L);

        Object array = converter.fromJson(element, null);
        assertTrue(array instanceof float[]);
        assertEquals(3f, ((float[])array)[0]);
        assertEquals(5f, ((float[])array)[1]);
        assertEquals(6f, ((float[])array)[2]);
        assertEquals(7f, ((float[])array)[3]);
    }
}

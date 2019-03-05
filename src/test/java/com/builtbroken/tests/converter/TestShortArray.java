package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayLong;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayShort;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestShortArray extends TestCase
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayShort converter = new JsonConverterArrayShort();
        short[] array = new short[]{2, 5, 9, 1};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals((short)2, element.getAsJsonArray().get(0).getAsShort());
        assertEquals((short)5, element.getAsJsonArray().get(1).getAsShort());
        assertEquals((short)9, element.getAsJsonArray().get(2).getAsShort());
        assertEquals((short)1, element.getAsJsonArray().get(3).getAsShort());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayShort converter= new JsonConverterArrayShort();
        JsonArray element = new JsonArray();
        element.add(3L);
        element.add(5L);
        element.add(6L);
        element.add(7L);

        Object array = converter.fromJson(element, null);
        assertTrue(array instanceof short[]);
        assertEquals((short)3, ((short[])array)[0]);
        assertEquals((short)5, ((short[])array)[1]);
        assertEquals((short)6, ((short[])array)[2]);
        assertEquals((short)7, ((short[])array)[3]);
    }
}

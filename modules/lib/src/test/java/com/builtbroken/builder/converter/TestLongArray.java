package com.builtbroken.builder.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayLong;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestLongArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayLong converter = new JsonConverterArrayLong();
        long[] array = new long[]{2L, 5L, 9L, 1L};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals(2L, element.getAsJsonArray().get(0).getAsLong());
        Assertions.assertEquals(5L, element.getAsJsonArray().get(1).getAsLong());
        Assertions.assertEquals(9L, element.getAsJsonArray().get(2).getAsLong());
        Assertions.assertEquals(1L, element.getAsJsonArray().get(3).getAsLong());
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
        Assertions.assertTrue(array instanceof long[]);
        Assertions.assertEquals(3L, ((long[])array)[0]);
        Assertions.assertEquals(5L, ((long[])array)[1]);
        Assertions.assertEquals(6L, ((long[])array)[2]);
        Assertions.assertEquals(7L, ((long[])array)[3]);
    }
}

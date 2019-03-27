package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayInt;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestIntegerArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayInt converter = new JsonConverterArrayInt();
        int[] array = new int[]{2, 5, 9, 1};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals(2, element.getAsJsonArray().get(0).getAsInt());
        Assertions.assertEquals(5, element.getAsJsonArray().get(1).getAsInt());
        Assertions.assertEquals(9, element.getAsJsonArray().get(2).getAsInt());
        Assertions.assertEquals(1, element.getAsJsonArray().get(3).getAsInt());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayInt converter = new JsonConverterArrayInt();
        JsonArray element = new JsonArray();
        element.add(3);
        element.add(5);
        element.add(6);
        element.add(7);

        Object array = converter.fromJson(element, null);
        Assertions.assertTrue(array instanceof int[]);
        Assertions.assertEquals(3, ((int[])array)[0]);
        Assertions.assertEquals(5, ((int[])array)[1]);
        Assertions.assertEquals(6, ((int[])array)[2]);
        Assertions.assertEquals(7, ((int[])array)[3]);
    }
}

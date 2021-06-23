package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayByte;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestByteArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayByte converter = new JsonConverterArrayByte();
        byte[] array = new byte[]{2, 5, 9, 1};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals((byte) 2, element.getAsJsonArray().get(0).getAsByte());
        Assertions.assertEquals((byte) 5, element.getAsJsonArray().get(1).getAsByte());
        Assertions.assertEquals((byte) 9, element.getAsJsonArray().get(2).getAsByte());
        Assertions.assertEquals((byte) 1, element.getAsJsonArray().get(3).getAsByte());
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
        Assertions.assertTrue(array instanceof byte[]);
        Assertions.assertEquals((byte) 3, ((byte[]) array)[0]);
        Assertions.assertEquals((byte) 5, ((byte[]) array)[1]);
        Assertions.assertEquals((byte) 6, ((byte[]) array)[2]);
        Assertions.assertEquals((byte) 7, ((byte[]) array)[3]);
    }
}

package com.builtbroken.builder.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayShort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestShortArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayShort converter = new JsonConverterArrayShort();
        short[] array = new short[]{2, 5, 9, 1};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals((short)2, element.getAsJsonArray().get(0).getAsShort());
        Assertions.assertEquals((short)5, element.getAsJsonArray().get(1).getAsShort());
        Assertions.assertEquals((short)9, element.getAsJsonArray().get(2).getAsShort());
        Assertions.assertEquals((short)1, element.getAsJsonArray().get(3).getAsShort());
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
        Assertions.assertTrue(array instanceof short[]);
        Assertions.assertEquals((short)3, ((short[])array)[0]);
        Assertions.assertEquals((short)5, ((short[])array)[1]);
        Assertions.assertEquals((short)6, ((short[])array)[2]);
        Assertions.assertEquals((short)7, ((short[])array)[3]);
    }
}

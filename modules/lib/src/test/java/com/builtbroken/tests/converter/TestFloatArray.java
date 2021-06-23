package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayFloat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestFloatArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayFloat converter = new JsonConverterArrayFloat();
        float[] array = new float[]{2f, 5f, 9f, 1f};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals(2f, element.getAsJsonArray().get(0).getAsFloat());
        Assertions.assertEquals(5f, element.getAsJsonArray().get(1).getAsFloat());
        Assertions.assertEquals(9f, element.getAsJsonArray().get(2).getAsFloat());
        Assertions.assertEquals(1f, element.getAsJsonArray().get(3).getAsFloat());
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
        Assertions.assertTrue(array instanceof float[]);
        Assertions.assertEquals(3f, ((float[])array)[0]);
        Assertions.assertEquals(5f, ((float[])array)[1]);
        Assertions.assertEquals(6f, ((float[])array)[2]);
        Assertions.assertEquals(7f, ((float[])array)[3]);
    }
}

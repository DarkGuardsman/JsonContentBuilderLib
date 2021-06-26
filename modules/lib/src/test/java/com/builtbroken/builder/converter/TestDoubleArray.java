package com.builtbroken.builder.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayDouble;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestDoubleArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayDouble converter = new JsonConverterArrayDouble();
        double[] array = new double[]{2.0, 5.0, 9.0, 1.0};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals(2.0, element.getAsJsonArray().get(0).getAsDouble());
        Assertions.assertEquals(5.0, element.getAsJsonArray().get(1).getAsDouble());
        Assertions.assertEquals(9.0, element.getAsJsonArray().get(2).getAsDouble());
        Assertions.assertEquals(1.0, element.getAsJsonArray().get(3).getAsDouble());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayDouble converter= new JsonConverterArrayDouble();
        JsonArray element = new JsonArray();
        element.add(3.0);
        element.add(5.0);
        element.add(6.0);
        element.add(7.0);

        Object array = converter.fromJson(element, null);
        Assertions.assertTrue(array instanceof double[]);
        Assertions.assertEquals(3.0, ((double[])array)[0]);
        Assertions.assertEquals(5.0, ((double[])array)[1]);
        Assertions.assertEquals(6.0, ((double[])array)[2]);
        Assertions.assertEquals(7.0, ((double[])array)[3]);
    }
}

package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayDouble;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayFloat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestDoubleArray extends TestCase
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayDouble converter = new JsonConverterArrayDouble();
        double[] array = new double[]{2.0, 5.0, 9.0, 1.0};
        JsonElement element = converter.toJson(array, null);

        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        assertEquals(2.0, element.getAsJsonArray().get(0).getAsDouble());
        assertEquals(5.0, element.getAsJsonArray().get(1).getAsDouble());
        assertEquals(9.0, element.getAsJsonArray().get(2).getAsDouble());
        assertEquals(1.0, element.getAsJsonArray().get(3).getAsDouble());
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
        assertTrue(array instanceof double[]);
        assertEquals(3.0, ((double[])array)[0]);
        assertEquals(5.0, ((double[])array)[1]);
        assertEquals(6.0, ((double[])array)[2]);
        assertEquals(7.0, ((double[])array)[3]);
    }
}

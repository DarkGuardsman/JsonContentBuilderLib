package com.builtbroken.builder.converter;

import com.builtbroken.builder.converter.strut.array.JsonConverterArrayString;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public class TestStringArray
{

    @Test
    public void testToJson()
    {
        JsonConverterArrayString converter = new JsonConverterArrayString();
        String[] array = new String[]{"a", "b", "e", "1"};
        JsonElement element = converter.toJson(array, null);

        Assertions.assertTrue(element instanceof JsonArray);
        Assertions.assertEquals(4, element.getAsJsonArray().size());
        Assertions.assertEquals("a", element.getAsJsonArray().get(0).getAsString());
        Assertions.assertEquals("b", element.getAsJsonArray().get(1).getAsString());
        Assertions.assertEquals("e", element.getAsJsonArray().get(2).getAsString());
        Assertions.assertEquals("1", element.getAsJsonArray().get(3).getAsString());
    }

    @Test
    public void testFromJson()
    {
        JsonConverterArrayString converter = new JsonConverterArrayString();
        JsonArray element = new JsonArray();
        element.add("g");
        element.add("a");
        element.add("f");
        element.add("1");

        Object array = converter.fromJson(element, null);
        Assertions.assertTrue(array instanceof String[]);
        Assertions.assertEquals("g", ((String[])array)[0]);
        Assertions.assertEquals("a", ((String[])array)[1]);
        Assertions.assertEquals("f", ((String[])array)[2]);
        Assertions.assertEquals("1", ((String[])array)[3]);
    }
}

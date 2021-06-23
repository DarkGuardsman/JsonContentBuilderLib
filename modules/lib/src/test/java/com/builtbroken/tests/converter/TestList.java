package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.converter.primitives.JsonConverterString;
import com.builtbroken.builder.converter.strut.list.JsonConverterList;
import com.builtbroken.builder.converter.strut.map.JsonConverterMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public class TestList
{
    @Test
    public void testToJsonString()
    {
        //Converter
        final JsonConverterList converter = new JsonConverterList();
        converter.onRegistered(new ConversionHandler(null, "test")
                .addConverter(new JsonConverterString()));

        //Test data
        final JsonArray array = new JsonArray();
        array.add("a");
        array.add("b");
        array.add("c");
        array.add("d");

        //Convert
        List list = converter.fromJson(array, new String[]{ConverterRefs.STRING});

        Assertions.assertNotNull(list);
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals("a", list.get(0));
        Assertions.assertEquals("b", list.get(1));
        Assertions.assertEquals("c", list.get(2));
        Assertions.assertEquals("d", list.get(3));

    }

    @Test
    public void testFromJsonString()
    {
        //Converter
        final JsonConverterList converter = new JsonConverterList();
        converter.onRegistered(new ConversionHandler(null, "test")
                .addConverter(new JsonConverterString()));

        //Test data
        final List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        //Convert
        JsonArray array = converter.toJson(list, new String[]{ConverterRefs.STRING});

        Assertions.assertNotNull(list);
        Assertions.assertEquals(4, array.size());
        Assertions.assertEquals("a", array.get(0).getAsString());
        Assertions.assertEquals("b", array.get(1).getAsString());
        Assertions.assertEquals("c", array.get(2).getAsString());
        Assertions.assertEquals("d", array.get(3).getAsString());
    }
}

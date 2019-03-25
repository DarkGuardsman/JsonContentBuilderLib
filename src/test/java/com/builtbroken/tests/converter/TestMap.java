package com.builtbroken.tests.converter;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.primitives.JsonConverterString;
import com.builtbroken.builder.converter.strut.array.JsonConverterArrayString;
import com.builtbroken.builder.converter.strut.map.JsonConverterMap;
import com.builtbroken.builder.mapper.JsonObjectWiring;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-25.
 */
public class TestMap extends TestCase
{

    @Test
    public void testToJson()
    {
        //Create converter
        JsonConverterMap converter = new JsonConverterMap();
        converter.onRegistered(new ConversionHandler(null, "test")
                .addConverter(new JsonConverterString()));

        //Create map
        Map<String, String> map = new HashMap();
        map.put("a", "z");
        map.put("b", "1");
        map.put("c", "2");
        map.put("d", "3");

        //Convert
        JsonElement element = converter.toJson(map, new String[]{"string", "string"});

        //Test
        assertTrue(element instanceof JsonArray);
        assertEquals(4, element.getAsJsonArray().size());
        checkKey(element, 0, "a");
        checkKey(element, 1, "b");
        checkKey(element, 2, "c");
        checkKey(element, 3, "d");

        checkValue(element, 0, "z");
        checkValue(element, 1, "1");
        checkValue(element, 2, "2");
        checkValue(element, 3, "3");
    }

    private void checkKey(JsonElement element, int index, String match)
    {
        assertTrue("Should be a JSON object for index " + index + " in " + element,
                element.getAsJsonArray().get(index).isJsonObject());
        assertTrue("Should contain a field 'key' for object at index " + index + " in " + element,
                element.getAsJsonArray().get(index).getAsJsonObject().has("key"));
        assertEquals(match, element.getAsJsonArray().get(index).getAsJsonObject().get("key").getAsString());
    }

    private void checkValue(JsonElement element, int index, String match)
    {
        assertTrue("Should be a JSON object for index " + index + " in " + element,
                element.getAsJsonArray().get(index).isJsonObject());
        assertTrue("Should contain a field 'value' for object at index " + index + " in " + element,
                element.getAsJsonArray().get(index).getAsJsonObject().has("value"));
        assertEquals(match, element.getAsJsonArray().get(index).getAsJsonObject().get("value").getAsString());
    }

    @Test
    public void testFromJson()
    {
        //Create converter
        JsonConverterMap converter = new JsonConverterMap();
        converter.onRegistered(new ConversionHandler(null, "test")
                .addConverter(new JsonConverterString()));

        //Create map
        JsonArray element = new JsonArray();
        element.add(newMapEntry("a", "z"));
        element.add(newMapEntry("b", "1"));
        element.add(newMapEntry("c", "2"));
        element.add(newMapEntry("d", "3"));

        //Convert
        Map map = converter.fromJson(element, new String[]{"string", "string"});

        //Test
        assertTrue(map instanceof Map);
        assertEquals(4, map.size());
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
        assertTrue(map.containsKey("c"));
        assertTrue(map.containsKey("d"));

        assertEquals("z", map.get("a"));
        assertEquals("1", map.get("b"));
        assertEquals("2", map.get("c"));
        assertEquals("3", map.get("d"));
    }

    private JsonObject newMapEntry(String key, String value)
    {
        JsonObject object = new JsonObject();
        object.addProperty("key", key);
        object.addProperty("value", value);
        return object;
    }
}

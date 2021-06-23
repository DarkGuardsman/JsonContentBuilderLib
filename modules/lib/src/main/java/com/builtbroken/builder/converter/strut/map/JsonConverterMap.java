package com.builtbroken.builder.converter.strut.map;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple map converter, more complex version will be added later
 * <p>
 * Created by Robin Seifert on 2019-03-06.
 */
public class JsonConverterMap extends JsonConverter<Map>
{

    private ConversionHandler handler;

    public String json_key = "key";
    public String json_value = "value";

    public JsonConverterMap()
    {
        super("java:map", "map");
    }

    @Override
    public void onRegistered(ConversionHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public boolean canSupport(Object object)
    {
        return object instanceof Map;
    }

    @Override
    public boolean canSupport(JsonElement json)
    {
        return json.isJsonArray();
    }

    @Override
    public JsonElement toJson(Map map, String[] args)
    {
        //TODO allow args for key and value
        //  [key, value, key_size, key_args, value_args]
        //  ["string", "float", 1, 100, 2] could be 1 format option
        //  ["string:100", "float:2"] could be another option but would require splitting
        //  this also means more complex entries would need a separator
        //  could cache this from the annotation if needed?
        if (args == null || args.length != 2)
        {
            throw new IllegalArgumentException("JsonConverterArray: args are required to define conversion type");
        }

        //Get data
        final String key_type = args[0].toLowerCase();
        final String value_type = args[1].toLowerCase();

        //Generate array
        final JsonArray jsonArray = new JsonArray();
        for (Object object : map.entrySet())
        {
            //Get key and value
            final Object key = ((Map.Entry) object).getKey();
            final Object value = ((Map.Entry) object).getValue();

            //Generate json object to act as storage
            final JsonObject jsonObject = new JsonObject();
            final JsonElement key_element = handler.toJson(key_type, key, null);
            final JsonElement value_element = handler.toJson(value_type, value, null);

            //Key and value json should not be null, value can only be null if orginal value was null
            if (key_element != null && (value_element != null || value == null))
            {
                jsonObject.add(json_key, key_element);
                jsonObject.add(json_value, value_element);
                jsonArray.add(jsonObject);
            }
            else
            {
                System.out.println("JsoNConverterArray: Failed to generate key-value pair. "
                        + "Key= " + key + " Key_Type= " + key_type
                        + "Value= " + value + " Value_Type= " + value_type);
                //TODO throw error if strict mode
            }
        }
        return jsonArray;
    }

    @Override
    public Map fromJson(JsonElement input, String[] args)
    {
        if (args == null || args.length != 2)
        {
            throw new IllegalArgumentException("JsonConverterArray: args are required to define conversion type");
        }

        //Get data
        final String key_type = args[0].toLowerCase();
        final String value_type = args[1].toLowerCase();

        //Generate array
        final JsonArray jsonArray = input.getAsJsonArray();
        final Map map = new HashMap();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            final JsonElement element = jsonArray.get(i);
            if (element.isJsonObject())
            {
                final JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has(json_key) && jsonObject.has(json_value))
                {
                    final Object key = handler.fromJson(key_type, jsonObject.get(json_key), null);
                    final Object value = handler.fromJson(value_type, jsonObject.get(json_value), null);

                    //Key can't be null, value can though really shouldn't
                    if (key != null)
                    {
                        map.put(key, value);
                    }
                    else
                    {
                        //TODO error
                    }
                }
                else
                {
                    //TODO error
                }
            }
            else
            {
                //TODO error
            }

        }
        return map;
    }
}

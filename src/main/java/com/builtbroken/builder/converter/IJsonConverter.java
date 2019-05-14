package com.builtbroken.builder.converter;

import com.builtbroken.builder.data.IJsonUnique;
import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public interface IJsonConverter<O extends Object> extends IJsonUnique
{
    /**
     * Get alterative names this convert can use.
     * <p>
     * This is used for providing shorter names for
     * use in annotations and conversion handlers.
     * <p>
     * Ex:
     * array -> java:array
     * int -> java:integer
     * map -> java:map.hash
     * set -> java:set.hash
     *
     * @return array of alt names to use
     */
    default String[] getAlias()
    {
        return null;
    }

    /**
     * Called to check if the object matches
     * the expected input. Called before
     * {@link #toJson(Object, String[])} in most cases
     *
     * @param object - object
     * @return true if can support
     */
    boolean canSupport(Object object);

    /**
     * Called to check if the json is supported
     * by this converter.
     *
     * @param json - json element
     * @return true if can support
     */
    boolean canSupport(JsonElement json);

    /**
     * Called to convert the object to JSON
     * <p>
     * Args are used to handle subtypes of this converter.
     * Best example are arrays that need to know what type of array to convert
     *
     * @param object - object to convert to json
     * @param args   - optional, extra args to define special handling
     * @return
     */
    default JsonElement toJson(O object, String[] args)
    {
        return toJson(object);
    }

    /**
     * Called to convert the object to JSON
     * <p>
     * Do not call this method instead use {@link #toJson(Object, String[])}
     *
     * @param object - object to convert to json
     * @return
     */
    default JsonElement toJson(O object)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Called to convert json to an object
     * <p>
     * Args are used to handle subtypes of this converter.
     * Best example are arrays that need to know what type of array to convert
     *
     * @param element - elemnent to convert from
     * @param args    - optional, extra args to define special handling
     * @return
     */
    default O fromJson(JsonElement element, String[] args)
    {
        return fromJson(element);
    }

    /**
     * Called to convert json to an object
     * <p>
     * Do not call this method instead use {@link #fromJson(JsonElement, String[])}
     *
     * @param element - elemnent to convert from
     * @return
     */
    default O fromJson(JsonElement element)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Called when the converter is added to the handler.
     * This can be used to get access to the handler and
     * other resources.
     *
     * @param handler
     */
    default void onRegistered(ConversionHandler handler)
    {

    }
}

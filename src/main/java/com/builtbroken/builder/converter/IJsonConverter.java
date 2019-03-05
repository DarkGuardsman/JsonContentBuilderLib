package com.builtbroken.builder.converter;

import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public interface IJsonConverter<O extends Object>
{

    /**
     * Used to ID the conversion type
     * <p>
     * Ex:
     * "software:type.subType"
     * "armory:ammo.type"
     * "atomicscience:fuel.rod"
     * "java:array"
     * "java:map.hash"
     *
     * @return unique id
     */
    String getUniqueID();

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
     * {@link #toJson(Object)} in most cases
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
     *
     * @param object
     * @return
     */
    JsonElement toJson(O object);

    /**
     * Called to convert json to an object
     *
     * @param element
     * @return
     */
    O fromJson(JsonElement element);
}

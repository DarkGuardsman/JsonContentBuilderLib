package com.builtbroken.builder.data;

/**
 * Applied to objects that will be generated from a
 * JSON template and registered to a handler for use in other systems.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public interface IJsonGeneratedObject
{

    /**
     * Type of JSON template this object was generated using
     */
    String getJsonType();

    /**
     * Unique ID withing the {@link #getJsonType()}
     */
    String getJsonUniqueID();

    /**
     * Called to check if the object was built correctly.
     * <p>
     * Will be called in post build phase to do final
     * check that may require testing auto wiring
     * and if data is within expected limits.
     *
     * @return true if is valid
     */
    default boolean isValid()
    {
        return true; //TODO create system to provide reason for why its invalid
    }
}

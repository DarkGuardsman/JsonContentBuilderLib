package com.builtbroken.builder.data;

/**
 * Applied to objects that need very simple [YES/NO] validation
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-16.
 */
public interface ISimpleDataValidation
{

    /**
     * Called to check if the object was built correctly.
     * <p>
     * Will be called in post build phase to do final
     * check that may require testing auto wiring
     * and if data is within expected limits.
     *
     * @return true if is valid
     */
    boolean isValid();
}

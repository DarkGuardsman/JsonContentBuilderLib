package com.builtbroken.builder.data;

/**
 * Created by Robin Seifert on 2019-05-14.
 */
public interface IJsonUnique
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
}

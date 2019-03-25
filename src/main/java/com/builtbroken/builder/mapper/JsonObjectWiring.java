package com.builtbroken.builder.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate that an object needs
 * to be linked post creation of all objects. During the
 * loading phase all objects will be generated, mapped with basic data,
 * and then when all objects are created will links be setup.
 * <p>
 * It is expected that the input data is a primitive string. As
 * this will be paired with the type to find the object to wire
 * into the field/method.
 * <p>
 * {@link #objectType()} is expected to be the matching group of objects
 * that the input information will be present inside. When objects
 * are created they are created under a builder with a set ID. This
 * ID is the type that needs to be matched and should be unique
 * per object.
 * <p>
 * Example would be a game that produced ammo from JSON for the player's
 * weapons. The ID would be "ammo" and may produce the items (ammo:shotgun, ammo:handgun, ammo:rpg).
 * <p>
 * This ammo may even have its own auto wire for type
 * "ammo.type:shotgun"
 *
 *
 *
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD})
public @interface JsonObjectWiring
{

    /**
     * List of keys to use for matching JSON
     */
    String[] jsonFields();

    /**
     * Type of system to lookup value inside.
     * <p>
     * This will be matched with input data to
     * locate the object from its parent registry.
     * <p>
     * Ex: "armory:gun"
     */
    String objectType();

    /**
     * Enforced that a value is not null and contains data
     * <p>
     * Only works on fields at this time.
     *
     * @return true to enforce a not null state
     */
    boolean required() default false;
}

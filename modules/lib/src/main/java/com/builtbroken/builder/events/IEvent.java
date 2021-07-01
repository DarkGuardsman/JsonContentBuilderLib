package com.builtbroken.builder.events;

import javax.annotation.Nonnull;

/**
 * Created by Robin Seifert on 6/30/2021.
 */
public interface IEvent
{
    /**
     * Type of event
     * @return event name
     */
    @Nonnull
    String getType();
}

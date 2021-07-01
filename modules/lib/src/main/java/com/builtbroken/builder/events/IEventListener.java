package com.builtbroken.builder.events;

/**
 * Created by Robin Seifert on 6/30/2021.
 */
public interface IEventListener<E extends IEvent>
{
    void consumeEvent(E event);

    String getType();
}

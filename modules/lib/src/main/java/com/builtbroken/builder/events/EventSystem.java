package com.builtbroken.builder.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Robin Seifert on 6/30/2021.
 */
public class EventSystem
{
    private final Map<String, List<IEventListener>> listeners = new HashMap();

    public void fireEvent(IEvent event) {
        getListeners(event.getType()).forEach(e -> e.consumeEvent(event));
    }

    public void addListener(IEventListener listener) {
        if(!listeners.containsKey(listener.getType())) {
            listeners.put(listener.getType(), new ArrayList());
        }
        listeners.get(listener.getType()).add(listener);
    }

    private List<IEventListener> getListeners(String type) {
        return listeners.getOrDefault(type, Collections.emptyList());
    }
}

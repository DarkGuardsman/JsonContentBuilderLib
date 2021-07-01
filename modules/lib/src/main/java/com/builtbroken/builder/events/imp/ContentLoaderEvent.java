package com.builtbroken.builder.events.imp;

import com.builtbroken.builder.events.IEvent;
import com.builtbroken.builder.loader.ContentLoader;

/**
 * Created by Robin Seifert on 6/30/2021.
 */
public abstract class ContentLoaderEvent implements IEvent
{
    private final ContentLoader loader;

    protected ContentLoaderEvent(ContentLoader loader) {
        this.loader = loader;
    }

    public ContentLoader getLoader() {
        return loader;
    }
}

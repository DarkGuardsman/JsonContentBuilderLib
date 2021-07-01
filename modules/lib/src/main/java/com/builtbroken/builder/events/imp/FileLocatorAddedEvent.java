package com.builtbroken.builder.events.imp;

import com.builtbroken.builder.events.EventTypes;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.loader.file.IFileLocator;

import javax.annotation.Nonnull;

/**
 * Created by Robin Seifert on 6/30/2021.
 */
public class FileLocatorAddedEvent extends ContentLoaderEvent
{
    private final IFileLocator fileLocator;

    public FileLocatorAddedEvent(ContentLoader loader, IFileLocator fileLocator)
    {
        super(loader);
        this.fileLocator = fileLocator;
    }

    public IFileLocator getFileLocator()
    {
        return fileLocator;
    }

    @Nonnull
    @Override
    public String getType()
    {
        return EventTypes.FILE_LOCATOR_ADDED.name();
    }
}

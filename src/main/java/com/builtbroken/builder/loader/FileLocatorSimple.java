package com.builtbroken.builder.loader;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;

import java.io.File;
import java.util.Collection;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-05.
 */
public class FileLocatorSimple implements IFileLocator
{
    public final File folderToSearch;

    public FileLocatorSimple(File folderToSearch)
    {
        this.folderToSearch = folderToSearch;
    }

    @Override
    public Collection<DataFileLoad> search()
    {
        return FileLoaderHandler.loadFile(folderToSearch);
    }
}

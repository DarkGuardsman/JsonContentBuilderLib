package com.builtbroken.builder.loader.file;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;

import java.io.File;
import java.util.Collection;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-05.
 */
public class FileLocatorFiltered implements IFileLocator
{
    public final File folderToSearch;

    public FileLocatorFiltered(File folderToSearch)
    {
        this.folderToSearch = folderToSearch;
    }

    @Override
    public Collection<DataFileLoad> search()
    {
        return FileLoaderHandler.loadFile(folderToSearch);
    }
}

package com.builtbroken.builder.loader.file;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Robin Seifert on 2019-04-05.
 */
public class FileLocatorFiltered implements IFileLocator
{
    public final File folderToSearch;

    private final LinkedList<FileCheckFunction> filterList = new LinkedList();
    private boolean allowList = true;

    public FileLocatorFiltered(File folderToSearch)
    {
        this.folderToSearch = folderToSearch;
    }

    @Override
    public Collection<DataFileLoad> search()
    {
        final List<DataFileLoad> dataFileLoadList = new ArrayList();
        FileLoaderHandler.loadFile(folderToSearch, (dataLoad) -> dataFileLoadList.add(dataLoad),
                (file, sub) -> filterList.stream().anyMatch(f -> f.loadFile(file, sub) == true) == allowList);
        return dataFileLoadList;
    }

    public FileLocatorFiltered allow()
    {
        allowList = true;
        return this;
    }

    public FileLocatorFiltered disallow()
    {
        allowList = false;
        return this;
    }

    public FileLocatorFiltered filter(FileCheckFunction function)
    {
        filterList.add(function);
        return this;
    }
}

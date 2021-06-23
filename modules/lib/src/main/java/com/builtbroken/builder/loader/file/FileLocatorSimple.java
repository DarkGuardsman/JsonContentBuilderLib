package com.builtbroken.builder.loader.file;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Robin Seifert on 2019-04-05.
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
        final List<DataFileLoad> dataFileLoadList = new ArrayList();
        FileLoaderHandler.loadFile(folderToSearch, (dataLoad) -> dataFileLoadList.add(dataLoad), null);
        return dataFileLoadList;
    }
}

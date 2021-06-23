package com.builtbroken.builder.loader.file;

import com.builtbroken.builder.data.DataFileLoad;

import java.util.Collection;

/**
 * Created by Robin Seifert on 2019-04-05.
 */
public interface IFileLocator
{

    /**
     * Called to search for and load files
     *
     * @return collection of loaded files
     */
    Collection<DataFileLoad> search();
}

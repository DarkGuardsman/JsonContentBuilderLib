package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class FileLoaderJSON implements IFileLoader
{

    @Override
    public String[] getSupportedExtensions()
    {
        return new String[]{"json"};
    }

    @Override
    public DataFileLoad loadFile(File file)
    {
        return null;
    }
}

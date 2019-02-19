package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;

import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public interface IFileLoader
{
    String[] getSupportedExtensions();

    DataFileLoad loadFile(File file);
}

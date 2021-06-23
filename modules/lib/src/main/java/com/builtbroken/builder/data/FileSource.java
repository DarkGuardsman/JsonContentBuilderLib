package com.builtbroken.builder.data;

import java.io.File;
import java.net.URL;

/**
 * Created by Robin Seifert on 2/19/19.
 */
public class FileSource
{

    public final String filePath;
    public final String fileName;
    public final String type;

    public FileSource(String filePath, String fileName, String type)
    {
        this.filePath = filePath;
        this.fileName = fileName;
        this.type = type;
    }

    public FileSource(URL resource)
    {
        filePath = resource.getFile();
        fileName = resource.getFile();
        type = "url";
    }

    public FileSource(File file)
    {
        filePath = file.getAbsoluteFile().getAbsolutePath();
        fileName = file.getName();
        type = "file";
    }

    @Override
    public String toString()
    {
        return "FileSource[" + filePath + ", " + fileName + ", " + type + "]";
    }
}

package com.builtbroken.builder.loader.file;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-08.
 */
@FunctionalInterface
public interface FileCheckFunction
{

    /**
     * Called to check if the file or it's sub part should load
     *
     * @param file          - main file or folder
     * @param subObjectPath - sub part, only used for files that contain nested files (Ex: Compressed zip)
     * @return true if it should load
     */
    boolean loadFile(File file, @Nullable String subObjectPath);
}

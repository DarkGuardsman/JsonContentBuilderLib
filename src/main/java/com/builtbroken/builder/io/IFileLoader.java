package com.builtbroken.builder.io;

import com.google.gson.JsonElement;

import java.io.Reader;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public interface IFileLoader
{

    String getSupportedExtension();

    List<JsonElement> load(Reader reader);
}

package com.builtbroken.builder.handler;

import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-11.
 */
public interface IJsonObjectHandler<O extends IJsonGeneratedObject>
{
    void onCreated(GeneratedObject object);

    O getObject(String unqueId);
}

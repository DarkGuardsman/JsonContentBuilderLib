package com.builtbroken.builder.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.mapper.anno.JsonTemplate;
import com.sun.tools.doclets.formats.html.markup.ContentBuilder;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class JsonTemplateMapper
{

    public static void locateAllTemplates(ClassLoader byClassLoader)
    {
        Class clKlass = byClassLoader.getClass();
        System.out.println("Classloader: " + clKlass.getCanonicalName());
        while (clKlass != java.lang.ClassLoader.class)
        {
            clKlass = clKlass.getSuperclass();
        }
        try
        {
            java.lang.reflect.Field fldClasses = clKlass
                    .getDeclaredField("classes");
            fldClasses.setAccessible(true);
            final Object[] objects = ((Vector) fldClasses.get(byClassLoader)).toArray();
            for (Object next : objects)
            {
                final Class clazz = (Class) next;
                if (clazz.isAssignableFrom(IJsonGeneratedObject.class))
                {
                    JsonTemplate template = (JsonTemplate) clazz.getAnnotation(JsonTemplate.class);
                    if (template != null)
                    {
                        System.out.println("JsonTemplateMapper: Located template on class: " + clazz);
                        final ContentLoader loader = ContentBuilderLib.getLoader(template.contentLoader());
                        if (loader != null)
                        {
                            loader.registerObjectTemplate(template.type(), (Class<IJsonGeneratedObject>) clazz, !template.useDefaultConstructor() ? null :
                                    (json) ->
                                    {
                                        try
                                        {
                                            return (IJsonGeneratedObject) clazz.newInstance();
                                        } catch (Exception e)
                                        {
                                            throw new RuntimeException("Failed to create new instance of "
                                                    + clazz + " for type " + template.type(), e);
                                        }
                                    });
                        }
                        else
                        {
                            throw new RuntimeException("JsontemplateMapper: Failed to locate content loader by name '"
                                    + template.contentLoader() + "' while processing annotation on clazz " + clazz);
                        }
                    }
                }
            }
        } catch (SecurityException e)
        {
            e.printStackTrace();
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}

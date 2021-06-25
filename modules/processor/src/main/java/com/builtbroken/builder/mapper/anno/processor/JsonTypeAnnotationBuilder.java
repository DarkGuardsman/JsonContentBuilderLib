package com.builtbroken.builder.mapper.anno.processor;

import com.builtbroken.builder.mapper.anno.JsonTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Builds JSON metadata based on templates discovered
 * Created by Robin Seifert on 6/22/2021.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class JsonTypeAnnotationBuilder extends AbstractProcessor
{
    private Set<String> templates = new HashSet();

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        return Collections.singleton(JsonTemplate.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
        if (env.processingOver())
        {
            writeMetadata();
            writeServiceFile();
        }
        else
        {
            collectTemplates(annotations, env);
        }
        return true;
    }

    private void collectTemplates(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
        for (Element element : env.getElementsAnnotatedWith(JsonTemplate.class))
        {

            // Enforce usage on classes only
            if (element.getKind() != ElementKind.CLASS)
            {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        String.format("Only classes can be annotated with @%s", JsonTemplate.class.getSimpleName()));
            }
            templates.add(element.toString());
        }
    }

    private void writeMetadata()
    {
        final Filer filer = processingEnv.getFiler();

        final String string = "templates.json";
        final FileObject fileObject;
        try
        {
            fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", string);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        final JsonObject object = new JsonObject();
        final JsonArray array = new JsonArray();
        templates.stream().forEach(array::add);
        object.add("list", array);

        try (Writer writer = fileObject.openWriter())
        {
            final Gson gson = new Gson();
            gson.toJson(object, new JsonWriter(writer));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void writeServiceFile()
    {
        final Filer filer = processingEnv.getFiler();
        final String resourceFile = "META-INF/services/com.builtbroken.builder.data.IJsonGeneratedObject";
        final FileObject fileObject;
        try
        {
            fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        try (Writer writer = fileObject.openWriter())
        {
            templates.forEach(template -> {
                try
                {
                    writer.write(template + "\n");
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Error writing line: " + template, e);
                }
            });
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

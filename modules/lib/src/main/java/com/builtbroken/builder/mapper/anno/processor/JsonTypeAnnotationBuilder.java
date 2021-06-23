package com.builtbroken.builder.mapper.anno.processor;

import com.builtbroken.builder.mapper.anno.JsonTemplate;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;

/**
 * Builds JSON metadata based on templates discovered
 * Created by Robin Seifert on 6/22/2021.
 */
public class JsonTypeAnnotationBuilder extends AbstractProcessor
{
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(JsonTemplate.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
        annotations.forEach(annotation -> {
            final Set<? extends Element> elements = env.getElementsAnnotatedWith(annotation);

            elements.stream()
                    .filter(TypeElement.class::isInstance)
                    .map(TypeElement.class::cast)
                    .map(TypeElement::getQualifiedName)
                    .map(name -> "Class " + name + " is annotated with " + annotation.getQualifiedName())
                    .forEach(System.out::println);
        });

        return true;
    }
}

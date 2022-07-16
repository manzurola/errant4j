package io.github.manzurola.errant4j.core;

import io.github.manzurola.errant4j.lang.en.EnAnnotatorFactory;
import io.github.manzurola.spacy4j.api.SpaCy;

import java.util.Map;

/**
 * A supplier of {@link Annotator} objects.
 */
public final class Errant {

    private static final Map<String, AnnotatorFactory> annotatorFactories;

    static {
        annotatorFactories = Map.of(
                "en", new EnAnnotatorFactory()
        );
    }

    private Errant() {
    }

    public static Annotator newAnnotator(String language, SpaCy spaCy) {
        if (annotatorFactories.containsKey(language)) {
            return annotatorFactories.get(language).create(spaCy);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported Errant language %s", language));
        }
    }

    public static Annotator forEnglish(SpaCy spaCy) {
        return newAnnotator("en", spaCy);
    }

}

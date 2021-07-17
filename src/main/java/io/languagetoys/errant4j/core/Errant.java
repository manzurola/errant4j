package io.languagetoys.errant4j.core;

import io.languagetoys.errant4j.lang.en.classify.EnClassifier;
import io.languagetoys.errant4j.lang.en.merge.EnMerger;
import io.languagetoys.spacy4j.api.SpaCy;

import java.util.Map;
import java.util.function.Function;

/**
 * A supplier of {@link Annotator} objects.
 */
public final class Errant {

    private static final Map<String, Function<SpaCy, Annotator>> annotators;

    private Errant() {
    }

    static {
        annotators = Map.of(
                "en", spaCy -> Annotator.of(spaCy, new EnMerger(), new EnClassifier())
        );
    }

    public static Annotator newAnnotator(String language, SpaCy spaCy) {
        if (annotators.containsKey(language)) {
            return annotators.get(language).apply(spaCy);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported Errant language %s", language));
        }
    }

}

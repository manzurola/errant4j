package io.languagetoys.errant4j.core;

import io.languagetoys.errant4j.lang.en.classify.EnClassifier;
import io.languagetoys.errant4j.lang.en.merge.EnMerger;
import io.languagetoys.spacy4j.api.SpaCy;

import java.util.Map;
import java.util.function.Function;

public final class Errant {

    private final Map<String, Function<SpaCy, Annotator>> annotators;

    private Errant() {
        this.annotators = Map.of(
                "en", spaCy -> Annotator.of(spaCy, new EnMerger(), new EnClassifier())
        );
    }

    public final Annotator annotator(String language, SpaCy spaCy) {
        if (annotators.containsKey(language)) {
            return annotators.get(language).apply(spaCy);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported Errant language %s", language));
        }
    }

    public static Errant create() {
        return new Errant();
    }

}

package io.github.manzurola.errant4j.core;

import io.github.manzurola.spacy4j.api.SpaCy;

public interface AnnotatorFactory {
    Annotator create(SpaCy spaCy);
}

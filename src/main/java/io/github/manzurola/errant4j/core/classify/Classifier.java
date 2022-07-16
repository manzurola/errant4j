package io.github.manzurola.errant4j.core.classify;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.spacy4j.api.containers.Token;


public interface Classifier {

    /**
     * Get the {@link GrammaticalError} for the supplied Edit.
     */
    GrammaticalError classify(Edit<Token> edit);

}

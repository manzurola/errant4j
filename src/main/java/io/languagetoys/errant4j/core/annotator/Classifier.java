package io.languagetoys.errant4j.core.annotator;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.spacy4j.api.containers.Token;


public interface Classifier {

    /**
     * Get the {@link GrammaticalError} for the supplied Edit.
     */
    GrammaticalError classify(Edit<Token> edit);
}

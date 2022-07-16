package io.github.manzurola.errant4j.lang.en.classify;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.spacy4j.api.containers.Token;

public interface ClassificationRule {
    GrammaticalError classify(Edit<Token> edit);
}

package io.github.manzurola.errant4j.lang.en.classify;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.spacy4j.api.containers.Token;

public abstract class ClassificationPredicate implements ClassificationRule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return test(edit) ?
               GrammaticalError.of(edit, getErrorCategory()) :
               GrammaticalError.unknown(edit);
    }

    public abstract ErrorCategory getErrorCategory();

    protected abstract boolean test(Edit<Token> edit);
}

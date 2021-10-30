package com.github.manzurola.errant4j.core.classify;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.errors.ErrorCategory;
import com.github.manzurola.errant4j.core.errors.GrammaticalError;
import com.github.manzurola.spacy4j.api.containers.Token;

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

package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.classify.ClassificationRule;
import com.github.manzurola.errant4j.core.errors.GrammaticalError;
import com.github.manzurola.spacy4j.api.containers.Token;

public class UnknownErrorCleanupRule implements ClassificationRule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return GrammaticalError.unknown(edit);
    }
}

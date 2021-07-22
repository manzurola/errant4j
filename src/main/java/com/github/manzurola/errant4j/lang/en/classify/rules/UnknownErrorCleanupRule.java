package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.spacy4j.api.containers.Token;

public class UnknownErrorCleanupRule implements Classifier.Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return GrammaticalError.unknown(edit);
    }
}

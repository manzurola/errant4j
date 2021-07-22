package com.github.manzurola.errant4j.core.classify;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;

final class ClassifierImpl implements Classifier {
    private final List<Rule> rules;

    ClassifierImpl(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public final GrammaticalError classify(Edit<Token> edit) {
        if (edit.matches(Predicates.isEqual())) {
            return GrammaticalError.NONE;
        }
        GrammaticalError error = null;
        for (Rule classifier : rules) {
            error = classifier.classify(edit);
            if (!error.category().equals(GrammaticalError.Category.OTHER)) {
                return error;
            }
        }
        return error;
    }
}

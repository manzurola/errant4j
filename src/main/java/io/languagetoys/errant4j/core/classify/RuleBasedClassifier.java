package io.languagetoys.errant4j.core.classify;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

class RuleBasedClassifier implements Classifier {
    private final List<Rule> rules;

    RuleBasedClassifier(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
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

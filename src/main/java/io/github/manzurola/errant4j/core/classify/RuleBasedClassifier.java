package io.github.manzurola.errant4j.core.classify;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;

public abstract class RuleBasedClassifier implements Classifier {

    @Override
    public final GrammaticalError classify(Edit<Token> edit) {
        if (edit.matches(Predicates.isEqual())) {
            return GrammaticalError.NONE;
        }
        GrammaticalError error = null;
        List<ClassificationRule> rules = getRules();
        for (ClassificationRule classifier : rules) {
            error = classifier.classify(edit);
            if (!error.category().equals(ErrorCategory.OTHER)) {
                return error;
            }
        }
        return error;
    }

    protected abstract List<ClassificationRule> getRules();
}

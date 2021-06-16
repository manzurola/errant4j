package edu.guym.errantj.lang.en.classiy.rules.core;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;
import java.util.function.Supplier;

import static edu.guym.errantj.core.errors.GrammaticalError.NONE;
import static edu.guym.aligner.edit.predicates.EditPredicates.isEqual;

public class RuleBasedClassifier implements Classifier {

    private final List<Rule> policy;

    public RuleBasedClassifier(Supplier<List<Rule>> supplier) {
        this.policy = supplier.get();
    }

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        if (edit.matches(isEqual())) {
            return NONE;
        }
        GrammaticalError error = null;
        for (Rule classifier : policy) {
            error = classifier.classify(edit);
            if (!error.category().equals(GrammaticalError.Category.OTHER)) {
                return error;
            }
        }
        return error;
    }
}

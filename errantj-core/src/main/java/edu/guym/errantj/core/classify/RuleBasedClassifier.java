package edu.guym.errantj.core.classify;

import edu.guym.errantj.core.classify.rules.Rule;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;
import java.util.function.Supplier;

import static edu.guym.errantj.core.classify.GrammaticalError.NONE;
import static io.squarebunny.aligner.edit.predicates.EditPredicates.isEqual;

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
            error = classifier.apply(edit);
            if (!error.category().equals(Category.OTHER)) {
                return error;
            }
        }
        return error;
    }
}

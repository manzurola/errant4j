package io.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;
import io.github.manzurola.spacy4j.api.features.Pos;

/**
 * The following special PART rule captures edits where the tagger or parser confuses a preposition for a particle or
 * vice versa; e.g. [(look) at → (look) for].
 * <p>
 * 1. There is exactly one token on both sides of the edit, and 2. (a) The set of POS tags for these tokens is PREP and
 * PART, or (b) The set of dependency labels for these tokens is prep and part.
 */
public class PartRule extends ClassificationPredicate {

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.PART;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        //TODO implement failover with dependencies
        return edit.filter(Predicates.ofSizeOneToOne())
                .filter(Predicates.PosTagSetEquals(Pos.PART, Pos.ADP))
                .isPresent();
    }
}

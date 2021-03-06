package io.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The following special PUNCT rule captures edits where a change in punctuation also affects the case of the following
 * word; e.g. [. Because → , because] and [Because → , because].
 */
public class PunctuationEffectRule extends ClassificationPredicate {

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.PUNCT;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        // 1. The lower cased form of the last token is the same on both sides, and
        // 2. All remaining tokens are punctuation.

        if (edit.matches(Predicates.isSubstitute())) {
            Token sourceLast = edit.source().last();
            Token targetLast = edit.target().last();
            List<Token> remaining = edit.stream()
                    .filter(word -> !word.equals(sourceLast) && !word.equals(targetLast))
                    .collect(Collectors.toList());

            return sourceLast.lower().equals(targetLast.lower()) &&
                   remaining.stream().allMatch(Predicates.isPunctuation());
        }

        return false;
    }
}

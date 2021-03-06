package io.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.errant4j.lang.en.utils.Collectors;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Contraction errors are mainly edits that involve expanding contractions to their full form; e.g. [n’t → not] or [’ve
 * → have]. The full list of contractions includes: {’d, ’ll, ’m, n’t, ’re, ’s, and ’ve}. They are captured by the
 * following rule:
 * <p>
 * 1. There is no more than one token on both sides of the edit, and 2. All tokens have the same POS, and 3. At least
 * one token on either side is a member of the above set of 7 contractions.
 */
public class ContractionRule extends ClassificationPredicate {

    private final Set<String> contractions = Set.of("'d", "'ll", "'m", "n't", "'re", "'s", "'ve");

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.CONTR;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit.filter(Predicates.ofSizeOneToOne())
                .filter(tokensShareSamePos())
                .filter(e -> e.stream().anyMatch(token -> contractions.contains(token.text())))
                .isPresent();
    }

    private Predicate<Edit<Token>> tokensShareSamePos() {
        return edit -> edit
                .stream()
                .map(Token::pos)
                .distinct()
                .collect(Collectors.oneOrNone())
                .isPresent();
    }

}

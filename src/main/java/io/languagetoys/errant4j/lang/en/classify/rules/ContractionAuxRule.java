package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.lang.en.classify.CategoryMatchRule;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An additional rule captures special case auxiliaries in contractions.
 * Specifically, can, shall and will are respectively shortened to ca, sha and wo in ca n’t, sha n’t and wo n’t.
 * To prevent them being flagged as spelling errors, the following rule captures cases such as [can → ca] or [wo → will].
 * 1. There is exactly one token on both sides of the edit, and
 * 2. The set of strings for these tokens is ca and can, sha and shall, or wo and will.
 */
public class ContractionAuxRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.CONTR;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(wordsAreAuxContractions())
                .isPresent();
    }

    public Predicate<Edit<Token>> wordsAreAuxContractions() {
        return edit -> {
            Set<String> words = edit
                    .stream()
                    .map(Token::lower)
                    .collect(Collectors.toSet());
            return (words.equals(Set.of("ca", "can")) ||
                    words.equals(Set.of("sha", "shall")) ||
                    words.equals(Set.of("wo", "will")));
        };
    }
}

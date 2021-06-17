package edu.guym.errantj.lang.en.classify.rules.tokentier;

import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.predicates.EditPredicates;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;

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
                .filter(EditPredicates.ofSizeOneToOne())
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

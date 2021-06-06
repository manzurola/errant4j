package edu.guym.errantj.lang.en.classiy.rules.tokentier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.predicates.EditPredicates;
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
public class ContractionAuxRule implements Rule {

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(EditPredicates.ofSizeOneToOne())
                .filter(wordsAreAuxContractions())
                .map(classify(Category.CONTR))
                .orElse(unknown(edit));
    }

    public Predicate<Edit<Token>> wordsAreAuxContractions() {
        return edit -> {
            Set<String> words = edit
                    .stream()
                    .map(Token::lowerCase)
                    .collect(Collectors.toSet());
            return (words.equals(Set.of("ca", "can")) ||
                    words.equals(Set.of("sha", "shall")) ||
                    words.equals(Set.of("wo", "will")));
        };
    }
}

package edu.guym.errantj.lang.en.classiy.rules.tokentier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import edu.guym.errantj.lang.en.classiy.common.TokenEditPredicates;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.predicates.EditPredicates;
import edu.guym.spacyj.api.containers.Token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Contraction errors are mainly edits that involve expanding contractions to their full form;
 * e.g. [n’t → not] or [’ve → have]. The full list of contractions includes: {’d, ’ll, ’m, n’t, ’re, ’s, and
 * ’ve}. They are captured by the following rule:
 */
public class ContractionRule implements Rule {

    /**
     * 1. There is no more than one token on both sides of the edit, and
     * 2. All tokens have the same POS, and
     * 3. At least one token on either side is a member of the above set of 7 contractions.
     */
    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit.filter(EditPredicates.ofSizeOneToOne())
                .filter(TokenEditPredicates.tokensShareSamePos())
                .filter(e -> e.stream().anyMatch(isContraction()))
                .map(classify(Category.CONTR))
                .orElse(unknown(edit));
    }

    public Predicate<Token> isContraction() {
        final Set<String> contractions = new HashSet<>(Arrays.asList("'d", "'ll", "'m", "n't", "'re", "'s", "'ve"));
        return word -> contractions.contains(word.text());

    }
}

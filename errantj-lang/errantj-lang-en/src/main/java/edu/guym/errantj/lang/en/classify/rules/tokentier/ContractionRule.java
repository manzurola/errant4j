package edu.guym.errantj.lang.en.classify.rules.tokentier;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.CategoryMatchRule;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.spacyj.api.containers.Token;

import java.util.Set;
import java.util.function.Predicate;

import static edu.guym.errantj.core.tools.Collectors.oneOrNone;

/**
 * Contraction errors are mainly edits that involve expanding contractions to their full form;
 * e.g. [n’t → not] or [’ve → have]. The full list of contractions includes: {’d, ’ll, ’m, n’t, ’re, ’s, and
 * ’ve}. They are captured by the following rule:
 * <p>
 * 1. There is no more than one token on both sides of the edit, and
 * 2. All tokens have the same POS, and
 * 3. At least one token on either side is a member of the above set of 7 contractions.
 */
public class ContractionRule extends CategoryMatchRule {

    private final Set<String> contractions = Set.of("'d", "'ll", "'m", "n't", "'re", "'s", "'ve");

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.CONTR;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit.filter(Predicates.ofSizeOneToOne())
                .filter(tokensShareSamePos())
                .filter(e -> e.stream().anyMatch(token -> contractions.contains(token.text())))
                .isPresent();
    }

    private Predicate<? super Edit<Token>> tokensShareSamePos() {
        return edit -> edit
                .stream()
                .map(Token::pos)
                .distinct()
                .collect(oneOrNone())
                .isPresent();
    }

}

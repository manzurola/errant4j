package edu.guym.errantj.lang.en.classify.rules.postier;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Pos;

import static edu.guym.aligner.edit.predicates.EditPredicates.ofSizeOneToOne;
import static edu.guym.errantj.lang.en.classify.rules.common.CommonPredicates.PosTagSetEquals;

/**
 *  The following special PART rule captures edits where the tagger or parser confuses
 *  a preposition for a particle or vice versa; e.g. [(look) at → (look) for].
 *
 *  1. There is exactly one token on both sides of the edit, and
 *  2. (a) The set of POS tags for these tokens is PREP and PART, or
 *  (b) The set of dependency labels for these tokens is prep and part.
 *
 */
public class PartRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.PART;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        //TODO implement failover with dependencies
        return edit.filter(ofSizeOneToOne())
                .filter(PosTagSetEquals(Pos.PART, Pos.ADP))
                .isPresent();
    }
}

package edu.guym.errantj.lang.en.classify.rules.cleanup;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Pos;

public class IgnoreSpaceErrorRule extends CategoryMatchRule {
    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.IGNORED;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit.stream().allMatch(token -> Pos.SPACE.matches(token.pos()));
    }
}

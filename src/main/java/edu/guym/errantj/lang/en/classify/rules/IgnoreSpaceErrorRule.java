package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.grammar.GrammaticalError;
import edu.guym.errantj.lang.en.classify.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;

public class IgnoreSpaceErrorRule extends CategoryMatchRule {
    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.IGNORED;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit.stream().allMatch(Token::isWhitespace);
    }
}

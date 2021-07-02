package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.lang.en.classify.CategoryMatchRule;
import io.languagetoys.spacy4j.api.containers.Token;

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

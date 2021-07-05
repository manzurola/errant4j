package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.spacy4j.api.containers.Token;

public class IgnoreSpaceErrorRule extends Classifier.Predicate {
    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.IGNORED;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit.stream().allMatch(Token::isWhitespace);
    }
}

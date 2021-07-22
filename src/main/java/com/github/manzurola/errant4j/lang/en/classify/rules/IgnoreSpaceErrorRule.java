package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.spacy4j.api.containers.Token;

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

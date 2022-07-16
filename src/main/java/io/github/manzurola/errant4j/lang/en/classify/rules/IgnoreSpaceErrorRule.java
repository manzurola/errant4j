package io.github.manzurola.errant4j.lang.en.classify.rules;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.spacy4j.api.containers.Token;

public class IgnoreSpaceErrorRule extends ClassificationPredicate {
    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.IGNORED;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit.stream().allMatch(Token::isWhitespace);
    }
}

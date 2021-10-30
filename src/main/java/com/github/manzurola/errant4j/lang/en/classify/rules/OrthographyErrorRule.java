package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import com.github.manzurola.errant4j.core.errors.ErrorCategory;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Although the definition of orthography can be quite broad, we use it here to only refer to edits that involve case
 * and/or whitespace changes; e.g. [first → First] or [Bestfriend → best friend]. 1. The lower cased form of both sides
 * of the edit with all whitespace removed results in the same string.
 */
public class OrthographyErrorRule extends ClassificationPredicate {

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.ORTH;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.isSubstitute())
                .filter(normalizedSidesAreEqual())
                .isPresent();
    }

    public Predicate<Edit<Token>> normalizedSidesAreEqual() {
        return edit -> {
            String sourceText = edit.source().stream()
                    .map(Token::lower)
                    .map(String::trim)
                    .collect(Collectors.joining());
            String targetText = edit.target().stream()
                    .map(Token::lower)
                    .map(String::trim)
                    .collect(Collectors.joining());
            return sourceText.equals(targetText);
        };
    }


}

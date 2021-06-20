package edu.guym.errantj.lang.en.classify.rules.tokentier;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static edu.guym.errantj.lang.en.classify.rules.common.Predicates.isSubstitute;

/**
 * Although the definition of orthography can be quite broad,
 * we use it here to only refer to edits that involve case and/or whitespace changes;
 * e.g. [first → First] or [Bestfriend → best friend].
 * 1. The lower cased form of both sides of the edit with all whitespace removed results in the same string.
 */
public class OrthographyErrorRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.ORTH;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(isSubstitute())
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

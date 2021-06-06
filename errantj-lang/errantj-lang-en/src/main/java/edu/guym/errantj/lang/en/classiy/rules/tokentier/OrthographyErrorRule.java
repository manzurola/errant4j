package edu.guym.errantj.lang.en.classiy.rules.tokentier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.isSubstitute;

/**
 * Although the definition of orthography can be quite broad,
 * we use it here to only refer to edits that involve case and/or whitespace changes;
 * e.g. [first → First] or [Bestfriend → best friend].
 * 1. The lower cased form of both sides of the edit with all whitespace removed results in the same string.
 */
public class OrthographyErrorRule implements Rule {

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(isSubstitute())
                .filter(normalizedSidesAreEqual())
                .map(classify(Category.ORTH))
                .orElse(unknown(edit));
    }

    public Predicate<Edit<Token>> normalizedSidesAreEqual() {
        return edit -> {
            String sourceText = edit.source().stream()
                    .map(Token::lowerCase)
                    .map(String::trim)
                    .collect(Collectors.joining());
            String targetText = edit.target().stream()
                    .map(Token::lowerCase)
                    .map(String::trim)
                    .collect(Collectors.joining());
            return sourceText.equals(targetText);
        };
    }


}

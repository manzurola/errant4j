package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.aligner.edit.Segment;
import com.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import com.github.manzurola.errant4j.core.errors.ErrorCategory;
import com.github.manzurola.spacy4j.api.containers.Token;
import com.github.manzurola.spacy4j.api.features.Pos;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates.isSubstitute;
import static com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates.ofMaxSize;

/**
 * The following special VERB rule captures edits involving infinitival to and/or phrasal verbs; e.g. [to eat → ε],
 * [consuming → to eat] and [look at → see].
 * <p>
 * 1. All tokens on both sides of the edit are either PART or VERB, and 2. The last token on each side has a different
 * lemma.
 */
public class VerbRule extends ClassificationPredicate {

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.VERB;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(isSubstitute())
                .filter(ofMaxSize(2, 2))
                .filter(tokensArePartOrVerb())
                .filter(lastTokensDifferLemma())
                .isPresent();
    }

    public Predicate<Edit<Token>> tokensArePartOrVerb() {
        return edit -> edit
                .stream()
                .map(Token::pos)
                .allMatch(pos -> Pos.PART.matches(pos) || Pos.VERB.matches(pos));
    }

    public Predicate<Edit<Token>> lastTokensDifferLemma() {
        return edit -> edit
                               .streamSegments(Segment::last, Segment::last)
                               .map(Token::lemma)
                               .collect(Collectors.toSet())
                               .size() != 1;
    }
}

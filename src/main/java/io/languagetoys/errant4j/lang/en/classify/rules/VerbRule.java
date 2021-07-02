package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.aligner.edit.Segment;
import io.languagetoys.errant4j.core.grammar.GrammaticalError;
import io.languagetoys.errant4j.lang.en.classify.CategoryMatchRule;
import io.languagetoys.spacy4j.api.containers.Token;
import io.languagetoys.spacy4j.api.features.Pos;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates.isSubstitute;
import static io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates.ofMaxSize;

/**
 * The following special VERB rule captures edits involving infinitival to and/or phrasal verbs;
 * e.g. [to eat → ε], [consuming → to eat] and [look at → see].
 * <p>
 * 1. All tokens on both sides of the edit are either PART or VERB, and
 * 2. The last token on each side has a different lemma.
 */
public class VerbRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
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

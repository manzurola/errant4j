package edu.guym.errantj.lang.en.classiy.rules.postier;

import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.UdPos;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.Segment;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.isSubstitute;
import static io.squarebunny.aligner.edit.predicates.EditPredicates.ofMaxSize;

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
                .allMatch(pos -> UdPos.PART.matches(pos) || UdPos.VERB.matches(pos));
    }

    public Predicate<Edit<Token>> lastTokensDifferLemma() {
        return edit -> edit
                .streamSegments(Segment::last, Segment::last)
                .map(Token::lemma)
                .collect(Collectors.toSet())
                .size() != 1;
    }
}

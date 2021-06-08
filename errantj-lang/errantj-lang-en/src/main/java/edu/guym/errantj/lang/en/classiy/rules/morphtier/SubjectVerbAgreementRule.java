package edu.guym.errantj.lang.en.classiy.rules.morphtier;

import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.PtbPos;
import edu.guym.spacyj.api.features.UdPos;
import io.squarebunny.aligner.edit.Edit;

import java.util.function.Predicate;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.ofSizeOneToOne;

/**
 * Subject-verb agreement errors involve edits where the grammatical number of the subject does not agree with the grammatical number of the verb; e.g. [(I) has → (I) have]. They are captured as follows:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. Both tokens have the same lemma, and
 * 3.   (a) Both tokens are was and were, or
 * (b) i. Both tokens are POS tagged as VERB, and
 * ii. At least one token is POS tagged as a 3rd-person present tense verb form (VBZ), or
 * (c) i. Both tokens do not have the same POS tag, and
 * ii. The corrected token is POS tagged as 3rd-person present tense verb form (VBZ).
 */
public class SubjectVerbAgreementRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB_SVA;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(ofSizeOneToOne())
                .filter(bothTokensAreWasAndWere().or(
                        caseB().or(caseC())
                ))
                .isPresent();
    }

    public Predicate<Edit<Token>> bothTokensAreWasAndWere() {
        return edit -> {
            String source = edit.source().first().lowerCase();
            String target = edit.target().first().lowerCase();
            return source.equals("was") && target.equals("were") ||
                    source.equals("were") && target.equals("was");
        };
    }

    public Predicate<Edit<Token>> caseB() {
        return edit -> edit
                .filter(e -> e.stream().map(Token::pos).allMatch(UdPos.VERB::matches))
                .filter(e -> e.stream().map(Token::tag).anyMatch(PtbPos.VBZ::matches))
                .isPresent();
    }

    public Predicate<Edit<Token>> caseC() {
        return edit -> edit
                .filter(tokensDontSharePos())
                .filter(e -> PtbPos.VBZ.matches(e.target().first().tag()))
                .isPresent();
    }

    public Predicate<Edit<Token>> tokensDontSharePos() {
        return edit -> !edit.source().first().tag().equals(edit.target().first().tag());
    }
}

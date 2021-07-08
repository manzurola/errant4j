package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;
import io.languagetoys.spacy4j.api.features.Pos;
import io.languagetoys.spacy4j.api.features.Tag;

import java.util.function.Predicate;

/**
 * Subject-verb agreement errors involve edits where the grammatical number of the subject does not agree with the
 * grammatical number of the verb; e.g. [(I) has → (I) have]. They are captured as follows: 1. There is exactly one
 * token on both sides of the edit, and 2. Both tokens have the same lemma, and 3.   (a) Both tokens are was and were,
 * or (b) i. Both tokens are POS tagged as VERB, and ii. At least one token is POS tagged as a 3rd-person present tense
 * verb form (VBZ), or (c) i. Both tokens do not have the same POS tag, and ii. The corrected token is POS tagged as
 * 3rd-person present tense verb form (VBZ).
 */
public class SubjectVerbAgreementRule extends Classifier.Predicate {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB_SVA;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(bothTokensAreWasAndWere().or(
                        caseB().or(caseC())
                ))
                .isPresent();
    }

    public Predicate<Edit<Token>> bothTokensAreWasAndWere() {
        return edit -> {
            String source = edit.source().first().lower();
            String target = edit.target().first().lower();
            return source.equals("was") && target.equals("were") ||
                   source.equals("were") && target.equals("was");
        };
    }

    public Predicate<Edit<Token>> caseB() {
        return edit -> edit
                .filter(e -> e.stream().map(Token::pos).allMatch(Pos.VERB::matches))
                .filter(e -> e.stream().map(Token::tag).anyMatch(Tag.VBZ::matches))
                .isPresent();
    }

    public Predicate<Edit<Token>> caseC() {
        return edit -> edit
                .filter(tokensDontSharePos())
                .filter(e -> Tag.VBZ.matches(e.target().first().tag()))
                .isPresent();
    }

    public Predicate<Edit<Token>> tokensDontSharePos() {
        return edit -> !edit.source().first().tag().equals(edit.target().first().tag());
    }
}

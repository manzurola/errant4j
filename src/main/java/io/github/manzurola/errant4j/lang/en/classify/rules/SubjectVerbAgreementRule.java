package io.github.manzurola.errant4j.lang.en.classify.rules;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;
import io.github.manzurola.spacy4j.api.features.Pos;
import io.github.manzurola.spacy4j.api.features.Tag;

import java.util.function.Predicate;

/**
 * Subject-verb agreement errors involve edits where the grammatical number of the subject does not agree with the
 * grammatical number of the verb; e.g. [(I) has → (I) have].
 * <p>
 * They are captured as follows:
 * <p>
 * 1. There is exactly one token on both sides of the edit, and
 * <p>
 * 2. Both tokens have the same lemma, and
 * <p>
 * 3. (a) Both tokens are was and were, or
 * <p>
 * (b) i. Both tokens are POS tagged as VERB, and
 * <p>
 * (b) ii. At least one token is POS tagged as a 3rd-person present tense verb form (VBZ), or
 * <p>
 * (c) i. Both tokens do not have the same POS tag, and
 * <p>
 * (c) ii. The corrected token is POS tagged as 3rd-person present tense verb form (VBZ).
 */
public class SubjectVerbAgreementRule extends ClassificationPredicate {

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.VERB_SVA;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(bothTokensHaveSameLemma())
                .filter(bothTokensAreWasAndWere()
                                .or(bothTokensAreVerbAndOneIsVbz())
                                .or(targetTokenIsVbz())
                )
                .isPresent();
    }

    public Predicate<Edit<Token>> bothTokensHaveSameLemma() {
        return edit -> {
            String sourceLemma = edit.source().first().lemma();
            String targetLemma = edit.target().first().lemma();
            return sourceLemma.equals(targetLemma);
        };
    }

    public Predicate<Edit<Token>> bothTokensAreWasAndWere() {
        return edit -> {
            String source = edit.source().first().lower();
            String target = edit.target().first().lower();
            return source.equals("was") && target.equals("were") ||
                   source.equals("were") && target.equals("was");
        };
    }

    public Predicate<Edit<Token>> bothTokensAreVerbAndOneIsVbz() {
        return edit -> edit
                .filter(e -> e.stream().map(Token::pos).allMatch(Pos.VERB::matches))
                .filter(e -> e.stream().map(Token::tag).anyMatch(Tag.VBZ::matches))
                .isPresent();
    }

    public Predicate<Edit<Token>> targetTokenIsVbz() {
        return edit -> edit
                .filter(tokensDontSharePos())
                .filter(e -> Tag.VBZ.matches(e.target().first().tag()))
                .isPresent();
    }

    public Predicate<Edit<Token>> tokensDontSharePos() {
        return edit -> !edit.source().first().tag().equals(edit.target().first().tag());
    }
}

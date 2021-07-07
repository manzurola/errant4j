package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.errant4j.core.annotate.Classifier;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.spacy4j.api.containers.Token;
import io.languagetoys.spacy4j.api.features.Dependency;
import io.languagetoys.spacy4j.api.features.Pos;

import java.util.function.Predicate;

/**
 * Adjective form edits involve changes between bare, comparative and superlative adjective forms; e.g. [big → biggest]
 * or [smaller → small]. They are captured as followed: 1. There is exactly one token on both sides of the edit, and 2.
 * Both tokens have the same lemma, and 3. (a) Both tokens are POS tagged as ADJ, or (b) Both tokens are parsed as acomp
 * or amod.
 * <p>
 * <p>
 * <p>
 * A second rule captures multi-token adjective form errors; e.g. [more big → bigger]: 1. There are no more than two
 * tokens on both sides of the edit, and 2. The first token on either side is more or most, and 3. The last token on
 * both sides has the same lemma.
 */
public class AdjectiveFormRule extends Classifier.Predicate {

    private final Lemmatizer lemmatizer;

    public AdjectiveFormRule(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.ADJ_FORM;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.isSubstitute())
                .filter(Predicates.ofSizeOneToOne())
                .filter(Predicates.lemmasIntersect(lemmatizer))
                .filter(tokensAreTaggedAsAdj().or(tokensDependenciesCompOrAmod()))
                .isPresent();
    }

    public Predicate<Edit<Token>> tokensAreTaggedAsAdj() {
        return edit -> edit
                .stream()
                .map(Token::pos)
                .allMatch(Pos.ADJ::matches);
    }

    public Predicate<Edit<Token>> tokensDependenciesCompOrAmod() {
        return edit -> edit
                .stream()
                .map(Token::dependency)
                .allMatch(label -> Dependency.CCOMP.matches(label) || Dependency.AMOD.matches(label));
    }
}

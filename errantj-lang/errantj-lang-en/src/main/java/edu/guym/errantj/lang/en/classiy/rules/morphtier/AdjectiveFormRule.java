package edu.guym.errantj.lang.en.classiy.rules.morphtier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import edu.guym.errantj.lang.en.classiy.common.TokenEditPredicates;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Dependency;
import edu.guym.spacyj.api.features.UdPos;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.predicates.EditPredicates;

import java.util.function.Predicate;

/**
 * Adjective form edits involve changes between bare, comparative and superlative adjective forms;
 * e.g. [big → biggest] or [smaller → small]. They are captured as followed:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. Both tokens have the same lemma, and
 * 3. (a) Both tokens are POS tagged as ADJ, or
 * (b) Both tokens are parsed as acomp or amod.
 * <p>
 * <p>
 * <p>
 * A second rule captures multi-token adjective form errors; e.g. [more big → bigger]:
 * 1. There are no more than two tokens on both sides of the edit, and
 * 2. The first token on either side is more or most, and
 * 3. The last token on both sides has the same lemma.
 */
public class AdjectiveFormRule implements Rule {

    private final Lemmatizer lemmatizer;

    public AdjectiveFormRule(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(EditPredicates.isSubstitute())
                .filter(EditPredicates.ofSizeOneToOne())
                .filter(TokenEditPredicates.lemmasIntersect(lemmatizer))
                .filter(tokensAreTaggedAsAdj().or(tokensDependenciesCompOrAmod()))
                .map(classify(Category.ADJ_FORM))
                .orElse(unknown(edit));
    }

    public Predicate<Edit<Token>> tokensAreTaggedAsAdj() {
        return edit -> edit
                .stream()
                .map(Token::pos)
                .allMatch(UdPos.ADJ::matches);
    }

    public Predicate<Edit<Token>> tokensDependenciesCompOrAmod() {
        return edit -> edit
                .stream()
                .map(Token::dependency)
                .allMatch(label -> Dependency.CCOMP.matches(label) || Dependency.AMOD.matches(label));
    }
}

package edu.guym.errantj.lang.en.classiy.rules.morphtier;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classiy.rules.core.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Pos;
import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.predicates.EditPredicates;

import java.util.function.Predicate;

/**
 * Noun number errors all involve count nouns that have been changed from singular to plural or vice versa;
 * e.g. [cat → cats] or [dogs → dog]. They are captured by the following rule:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. Both tokens have the same lemma, and
 * 3. Both tokens are POS tagged as NOUN.
 */
public class NounNumberErrorRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.NOUN_NUM;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(EditPredicates.ofSizeOneToOne())
                .filter(sameLemma())
                .filter(posTaggedAsNoun())
                .isPresent();
    }

    public Predicate<Edit<Token>> sameLemma() {
        return edit -> edit.source().first().lemma().equals(edit.target().first().lemma());
    }

    public Predicate<Edit<Token>> posTaggedAsNoun() {
        return edit -> edit.stream().map(Token::pos).allMatch(Pos.NOUN::matches);
    }
}

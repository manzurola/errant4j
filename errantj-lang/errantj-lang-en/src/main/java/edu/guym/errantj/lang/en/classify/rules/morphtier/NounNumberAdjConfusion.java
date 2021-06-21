package edu.guym.errantj.lang.en.classify.rules.morphtier;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.api.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.CategoryMatchRule;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Pos;
import edu.guym.spacyj.api.features.Tag;

import java.util.function.Predicate;

import static edu.guym.errantj.api.tools.Collectors.oneOrNone;

/**
 * Since it is fairly common for the POS tagger to confuse nouns that look like adjectives, e.g. musical,
 * a separate rule uses fine-grained POS tags to matchError mis-tagged edits such as [musical (ADJ) → musicals (NOUN)]:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. Both tokens have the same lemma, and
 * 3. The original token is POS tagged as ADJ, and
 * 4. The corrected token is POS tagged as a plural noun (NNS).
 * Note that this second rule was only found to be effective in the singular to plural direction and not the other way around.
 */
public class NounNumberAdjConfusion extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.NOUN_NUM;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(Predicates.isSubstitute())
                .filter(Predicates.ofSizeOneToOne())
                .filter(sameLemma())
                .filter(e -> e.source().map(Token::pos).allMatch(Pos.ADJ::matches))
                .filter(e -> e.target().map(Token::tag).allMatch(Tag.NNS::matches))
                .isPresent();
    }

    public Predicate<Edit<Token>> sameLemma() {
        return edit -> edit.stream().map(Token::lemma).collect(oneOrNone()).isPresent();
    }
}

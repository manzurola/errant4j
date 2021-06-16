package edu.guym.errantj.lang.en.classify.rules.tokentier;

import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.Rule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.aligner.edit.Edit;

import static edu.guym.aligner.edit.predicates.EditPredicates.isTranspose;


/**
 * We restrict our definition of word order errors to only include edits whose tokens exactly match on both sides of the edit;
 * e.g. [house white → white house]. We also investigated allowing majority matches, e.g. [I saw the man → the man saw me],
 * but found exact matches were qualitatively more reliable in practice.
 * <p>
 * Basically if edit is transpose than this is a word order classify
 */
public class WordOrderRule implements Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return edit
                .filter(isTranspose())
                .map(e -> GrammaticalError.REPLACEMENT_WORD_ORDER)
                .orElse(GrammaticalError.unknown(edit));
    }
}

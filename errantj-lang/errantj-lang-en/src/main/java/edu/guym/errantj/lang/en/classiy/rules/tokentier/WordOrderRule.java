package edu.guym.errantj.lang.en.classiy.rules.tokentier;

import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.isTranspose;


/**
 * We restrict our definition of word order errors to only include edits whose tokens exactly match on both sides of the edit;
 * e.g. [house white → white house]. We also investigated allowing majority matches, e.g. [I saw the man → the man saw me],
 * but found exact matches were qualitatively more reliable in practice.
 *
 * Basically if edit is transpose than this is a word order classify
 */
public class WordOrderRule implements Rule {

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(isTranspose())
                .map(edit1 -> GrammaticalError.REPLACEMENT_WORD_ORDER)
                .orElse(unknown(edit));
    }
}

package io.github.manzurola.errant4j.lang.en.classify.rules;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationRule;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;


/**
 * We restrict our definition of word order errors to only include edits whose tokens exactly match on both sides of the
 * edit; e.g. [house white → white house]. We also investigated allowing majority matches, e.g. [I saw the man → the man
 * saw me], but found exact matches were qualitatively more reliable in practice.
 * <p>
 * Basically if edit is transpose than this is a word order classify
 */
public class WordOrderRule implements ClassificationRule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return edit
                .filter(Predicates.isTranspose())
                .map(e -> GrammaticalError.REPLACEMENT_WORD_ORDER)
                .orElse(GrammaticalError.unknown(edit));
    }
}

package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.spacy4j.api.containers.Token;


/**
 * We restrict our definition of word order errors to only include edits whose tokens exactly match on both sides of the
 * edit; e.g. [house white → white house]. We also investigated allowing majority matches, e.g. [I saw the man → the man
 * saw me], but found exact matches were qualitatively more reliable in practice.
 * <p>
 * Basically if edit is transpose than this is a word order classify
 */
public class WordOrderRule implements Classifier.Rule {

    @Override
    public GrammaticalError classify(Edit<Token> edit) {
        return edit
                .filter(Predicates.isTranspose())
                .map(e -> GrammaticalError.REPLACEMENT_WORD_ORDER)
                .orElse(GrammaticalError.unknown(edit));
    }
}

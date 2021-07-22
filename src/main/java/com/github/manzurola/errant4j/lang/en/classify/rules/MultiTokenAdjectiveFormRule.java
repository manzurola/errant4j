package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.aligner.edit.Segment;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Adjective form edits involve changes between bare, comparative and superlative adjective forms;
 * <p>
 * A second rule captures multi-token adjective form errors; e.g. [more big → bigger]: 1. There are no more than two
 * tokens on both sides of the edit, and 2. The first token on either side is more or most, and 3. The last token on
 * both sides has the same lemma.
 */
public class MultiTokenAdjectiveFormRule extends Classifier.Predicate {

    private final Lemmatizer lemmatizer;

    public MultiTokenAdjectiveFormRule(Lemmatizer lemmatizer) {
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
                .filter(Predicates.ofMaxSize(2, 2))
                .filter(firstTokenIsMoreOrMost())
                .filter(lastTokenHasSameLemma())
                .isPresent();
    }

    public Predicate<Edit<Token>> firstTokenIsMoreOrMost() {
        return edit -> edit
                .streamSegments(Segment::first, Segment::first)
                .anyMatch(token -> Set.of("more", "most").contains(token.lower()));
    }

    public Predicate<Edit<Token>> lastTokenHasSameLemma() {
        return edit -> {
            Set<String> sourceLemmas = lemmatizer.lemmas(edit.source().last().text());
            Set<String> targetLemmas = lemmatizer.lemmas(edit.target().last().text());
            return !Collections.disjoint(sourceLemmas, targetLemmas);
        };
    }


}

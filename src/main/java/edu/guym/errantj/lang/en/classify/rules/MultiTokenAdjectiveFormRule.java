package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.Segment;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.CategoryMatchRule;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Adjective form edits involve changes between bare, comparative and superlative adjective forms;
 * <p>
 * A second rule captures multi-token adjective form errors; e.g. [more big → bigger]:
 * 1. There are no more than two tokens on both sides of the edit, and
 * 2. The first token on either side is more or most, and
 * 3. The last token on both sides has the same lemma.
 */
public class MultiTokenAdjectiveFormRule extends CategoryMatchRule {

    private final Lemmatizer lemmatizer;

    public MultiTokenAdjectiveFormRule(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.ADJ_FORM;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(Predicates.isSubstitute())
                .filter(Predicates.ofMaxSize(2, 2))
                .filter(firstTokensAnyMatch(moreOrMost()))
                .filter(lastTokensHasSameLemma())
                .isPresent();
    }

    public Predicate<? super Token> moreOrMost() {
        return word -> Set.of("more", "most").contains(word.lower());
    }

    public Predicate<? super Edit<Token>> firstTokensAnyMatch(Predicate<? super Token> condition) {
        return edit -> edit
                .streamSegments(Segment::first, Segment::first)
                .anyMatch(condition);
    }

    public Predicate<? super Edit<Token>> lastTokensHasSameLemma() {
        return edit -> {
            Set<String> sourceLemmas = lemmatizer.lemmas(edit.source().last().text());
            Set<String> targetLemmas = lemmatizer.lemmas(edit.target().last().text());
            return !Collections.disjoint(sourceLemmas, targetLemmas);
        };
    }


}

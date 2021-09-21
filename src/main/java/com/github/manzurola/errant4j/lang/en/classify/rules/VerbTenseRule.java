package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import com.github.manzurola.spacy4j.api.containers.Token;
import com.github.manzurola.spacy4j.api.features.Tag;

import java.util.function.Predicate;

/**
 * Verb tense errors are the most complicated out of all other classify types and thus require the most rules. The main
 * reason for this is because although tense can be inflectional, e.g. [eat → ate], it can also be expressed
 * periphrastically by means of auxiliary verbs; e.g. [ate → has eaten]. This does not mean all auxiliary verbs are
 * tense errors however, and auxiliary verbs can also be verb form or agreement errors; e.g. [(is) be (eaten) → (is)
 * being (eaten)] and [(it) are (eaten) → (it) is (eaten)]. The majority of tense errors are hence captured by the
 * following rules:
 * <p>
 * 1. There is exactly one token on both sides of the edit, and
 *
 * 2. (a)   i. Both tokens have the same lemma, and
 *
 *          ii.Both tokens are POS tagged as VERB, and
 *
 *          iii. At least one token is POS tagged as a past tense verb form (VBD), or
 *
 *    (b)   i. Both tokens have the same lemma, and
 *
 *          ii. Both tokens are POS tagged as VERB, and
 *
 *          iii. Both tokens are parsed as an auxiliary verb (aux or auxpass), or
 *
 *    (c)   i. Both tokens have the same lemma, and
 *
 *          ii. Both tokens do not have the same POS tag, and
 *
 *          iii. The corrected token is POS tagged as a past tense verb form (VBD), or
 *
 *    (d)   i. Both tokens do not have the same lemma, and
 *
 *          ii. Both tokens are parsed as an auxiliary verb (aux or auxpass).
 */
public class VerbTenseRule extends Classifier.Predicate {

    private final Lemmatizer lemmatizer;

    public VerbTenseRule(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB_TENSE;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(case1()
                                .or(case2())
                                .or(case3())
                                .or(case4())
                )
                .isPresent();
    }

    /**
     * i. Both tokens have the same lemma, and ii. Both tokens are POS tagged as VERB, and iii. At least one token is
     * POS tagged as a past tense verb form (VBD), or
     *
     * @return
     */
    public Predicate<Edit<Token>> case1() {
        return edit -> edit
                .filter(sameLemma())
                .filter(e -> e.stream().allMatch(Predicates.isVerb()))
                .filter(e -> e.stream().anyMatch(isPastTenseVerbForm()))
                .isPresent();
    }

    /**
     * i. Both tokens have the same lemma, and ii. Both tokens are POS tagged as VERB, and iii. Both tokens are parsed
     * as an auxiliary verb (aux or auxpass)
     */
    public Predicate<Edit<Token>> case2() {
        return edit -> edit
                .filter(sameLemma())
                .filter(e -> e.stream().allMatch(Predicates.isVerb()))
                .filter(e -> e.stream().allMatch(Predicates.isAuxVerb()))
                .isPresent();
    }

    /**
     * i. Both tokens have the same lemma, and ii. Both tokens do not have the same POS tag, and iii. The corrected
     * token is POS tagged as a past tense verb form (VBD), or
     *
     * @return
     */
    public Predicate<Edit<Token>> case3() {
        return edit -> edit
                .filter(sameLemma())
                .filter(e -> !e.source().first().tag().equals(e.target().first().tag()))
                .map(e -> e.target().first())
                .filter(isPastTenseVerbForm())
                .isPresent();
    }

    /**
     * i. Both tokens do not have the same lemma, and ii. Both tokens are parsed as an auxiliary verb (aux or auxpass).
     *
     * @return
     */
    public Predicate<Edit<Token>> case4() {
        return edit -> edit
                .filter(sameLemma().negate())
                .filter(e -> e.stream().allMatch(Predicates.isAuxVerb()))
                .isPresent();
    }

    public Predicate<Edit<Token>> sameLemma() {
        return Predicates.lemmasIntersect(lemmatizer);
    }

    private Predicate<Token> isPastTenseVerbForm() {
        return word -> Tag.VBD.matches(word.tag());
    }
}

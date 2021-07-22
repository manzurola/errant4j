package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import com.github.manzurola.spacy4j.api.containers.Token;
import com.github.manzurola.spacy4j.api.features.Tag;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Verb form errors involve corrections between members of the set of bare infinitive, to- infinitive, gerund and
 * participle forms; e.g. {eat, to eat, eating, eaten}. Since infinitives tend to have exactly the same form as
 * non-3rd-person present tense forms however (cf. ‘I want to eat cake’ versus ‘I eat cake’), we must use fine-grained
 * POS tags to differentiate between them.
 * <p>
 * The majority of verb form errors are hence captured by the following rule:
 * <p>
 * 1. There is exactly one token on both sides of the edit, and 2. Both tokens have the same lemma, and 3.  a)   i. Both
 * tokens are POS tagged as VERB, and ii. Both tokens are preceded by a dependent auxiliary verb, or b)   i.  Both
 * tokens are POS tagged as VERB, and ii. At least one token is POS tagged as a gerund (VBG) or participle (VBN), or c)
 * i.  Both tokens do not have the same POS tag, and ii. The corrected token is POS tagged as a gerund (VBG) or
 * participle (VBN).
 * <p>
 * Since tense and agreement always fall on the first auxiliary within a verb phrase, if any, this means all other 1:1
 * verb edits with the same lemma can only be form errors; e.g. [(has) eating → (has) eaten] and [(has) be (eaten) →
 * (has) been (eaten)]. In most other cases, a verb form error involves a gerund and/or participle. When the original
 * token POS tag is not a verb, we instead defer to the corrected token POS tag to classify the edit; e.g. [watch (NOUN)
 * → watching (VBG)].
 * <p>
 * Other types of verb form errors involve infinitival to. The next rule hence captures missing or unnecessary to
 * particles that are not prepositions:
 * <p>
 * 1. There is only one token on one side of the edit, and 2. That token is to, and 3. That token is POS tagged as PART,
 * and 4. That token is not parsed as prep.
 * <p>
 * Finally, infinitival to may also be involved in more complex, multi-token edits; e.g. [to eat → eating]. These are
 * captured by the following rule: 1. All tokens on both sides of the edit are POS tagged as PART or VERB, and 2. The
 * last token on both sides has the same lemma.
 */
public class VerbFormRule extends Classifier.Predicate {

    private final Lemmatizer lemmatizer;

    public VerbFormRule(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB_FORM;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(sameLemma())
                .filter(case1().or(case2()).or(case3()))
                .isPresent();
    }

    public Predicate<Edit<Token>> case1() {
        return edit -> edit
                .stream()
                .allMatch(Predicates.isVerb().and(precededByDependantAuxVerb()));
    }

    public Predicate<Edit<Token>> case2() {
        return edit -> {
            List<Token> tokens = edit.stream().collect(Collectors.toList());
            return tokens.stream().allMatch(Predicates.isVerb()) &&
                   tokens.stream().anyMatch(gerundOrParticiple());
        };
    }

    public Predicate<Edit<Token>> case3() {
        return edit -> {
            Token source = edit.source().first();
            Token target = edit.target().first();
            return !source.pos().equals(target.pos()) && gerundOrParticiple().test(target);
        };
    }

    public Predicate<Token> precededByDependantAuxVerb() {
        return token -> token
                .children()
                .stream()
                .filter(dep -> dep.index() < token.index())
                .anyMatch(Predicates.isAuxVerb());
    }

    public Predicate<Token> gerundOrParticiple() {
        return token -> Tag.VBG.matches(token.tag()) || Tag.VBN.matches(token.tag());
    }

    public Predicate<Edit<Token>> sameLemma() {
        return Predicates.lemmasIntersect(lemmatizer);
    }
}

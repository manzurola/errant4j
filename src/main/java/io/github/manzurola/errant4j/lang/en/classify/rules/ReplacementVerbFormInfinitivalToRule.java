package io.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.classify.ClassificationPredicate;
import io.github.manzurola.errant4j.core.errors.ErrorCategory;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;
import io.github.manzurola.spacy4j.api.features.Pos;

import java.util.function.Predicate;

/**
 * Verb form errors involve corrections between members of the set of bare infinitive, to- infinitive, gerund and
 * participle forms; e.g. {eat, to eat, eating, eaten}. Since infinitives tend to have exactly the same form as
 * non-3rd-person present tense forms however (cf. ‘I want to eat cake’ versus ‘I eat cake’), we must use fine-grained
 * POS tags to differentiate between them.
 * <p>
 * Finally, infinitival to may also be involved in more complex, multi-token edits; e.g. [to eat → eating]. These are
 * captured by the following rule: 1. All tokens on both sides of the edit are POS tagged as PART or VERB, and 2. The
 * last token on both sides has the same lemma.
 */
public class ReplacementVerbFormInfinitivalToRule extends
    ClassificationPredicate {

    @Override
    public ErrorCategory getErrorCategory() {
        return ErrorCategory.VERB_FORM;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.isSubstitute())
                .filter(allTokensArePartOrVerb())
                .filter(lastTokensHaveSameLemma())
                .isPresent();
    }

    public Predicate<Edit<Token>> allTokensArePartOrVerb() {
        return edit -> edit.stream()
                .allMatch(token -> Pos.PART.matches(token.pos()) || Pos.VERB.matches(token.pos()));
    }


    public Predicate<Edit<Token>> lastTokensHaveSameLemma() {
        return edit -> {
            Token lastSource = edit.source().last();
            Token lastTarget = edit.target().last();
            return lastSource.lemma().equals(lastTarget.lemma());
        };
    }


}

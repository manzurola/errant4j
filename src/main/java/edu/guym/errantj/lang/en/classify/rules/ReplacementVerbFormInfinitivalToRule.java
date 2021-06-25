package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.grammar.GrammaticalError;
import edu.guym.errantj.lang.en.classify.CategoryMatchRule;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.Pos;

import java.util.function.Predicate;

/**
 * Verb form errors involve corrections between members of the set of bare infinitive, to-
 * infinitive, gerund and participle forms; e.g. {eat, to eat, eating, eaten}.
 * Since infinitives tend to have exactly the same form as non-3rd-person present tense forms however
 * (cf. ‘I want to eat cake’ versus ‘I eat cake’), we must use fine-grained POS tags to differentiate between them.
 * <p>
 * Finally, infinitival to may also be involved in more complex, multi-token edits; e.g. [to eat → eating].
 * These are captured by the following rule:
 * 1. All tokens on both sides of the edit are POS tagged as PART or VERB, and
 * 2. The last token on both sides has the same lemma.
 */
public class ReplacementVerbFormInfinitivalToRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB_FORM;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
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

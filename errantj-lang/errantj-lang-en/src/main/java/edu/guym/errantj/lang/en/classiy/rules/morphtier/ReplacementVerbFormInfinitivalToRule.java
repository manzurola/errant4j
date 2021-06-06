package edu.guym.errantj.lang.en.classiy.rules.morphtier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.UdPos;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.predicates.EditPredicates;

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
public class ReplacementVerbFormInfinitivalToRule implements Rule {

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(EditPredicates.isSubstitute())
                .filter(allTokensArePartOrVerb())
                .filter(lastTokensHaveSameLemma())
                .map(classify(Category.VERB_FORM))
                .orElse(unknown(edit));
    }

    public Predicate<Edit<Token>> allTokensArePartOrVerb() {
        return edit -> edit.stream()
                .allMatch(token -> UdPos.PART.matches(token.pos()) || UdPos.VERB.matches(token.pos()));
    }


    public Predicate<Edit<Token>> lastTokensHaveSameLemma() {
        return edit -> {
            Token lastSource = edit.source().last();
            Token lastTarget = edit.target().last();
            return lastSource.lemma().equals(lastTarget.lemma());
        };
    }


}

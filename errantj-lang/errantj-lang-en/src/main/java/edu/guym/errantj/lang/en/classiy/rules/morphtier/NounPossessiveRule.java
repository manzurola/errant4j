package edu.guym.errantj.lang.en.classiy.rules.morphtier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.spacyj.api.features.PtbPos;
import io.squarebunny.aligner.edit.Edit;

import java.util.List;

import static io.squarebunny.aligner.edit.predicates.EditPredicates.ofSize;
import static java.util.stream.Collectors.toList;

/**
 * Noun possessive errors typically involve edits that change a possessive suffix on a noun phrase;
 * e.g. [(Tom) ε → (Tom) ’s] or [(Chris) ’s → (Chris) ’]. They are captured by the following rule:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. At least one side of the edit is POS tagged as a possessive suffix (POS)
 * <p>
 * While the above rule handles possessive suffixes that have become separated from their dependent nouns
 * as a result of an automatic alignment, the following rule handles multi-token edits where this is not the case;
 * e.g. [friends → friend ’s]:
 * 1. There are exactly two tokens on at least one side of the edit, and
 * 2. (a) The original tokens are POS tagged sequentially as NOUN and PART, or
 * (b) The corrected tokens are POS tagged sequentially as NOUN and PART, and
 * 3. The first token on both sides of the edit has the same lemma.
 */
public class NounPossessiveRule implements Rule {

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        if (edit.matches(ofSize(1, 0).or(ofSize(0, 1)))) {
            List<String> posSet = edit.stream().map(Token::tag).distinct().collect(toList());
            if (posSet.size() == 1 && isPossessiveCase(posSet.get(0))) {
                return classify(edit, Category.NOUN_POSS);
            }
        }

        if (edit.matches(ofSize(1, 2))) {

            String sourceLemma = edit.source().first().lemma();
            String targetLemma = edit.target().first().lemma();

            String possessiveCase = edit.target().first().next().map(Token::tag).orElse("");
            if (sourceLemma.equals(targetLemma) && isPossessiveCase(possessiveCase)) {
                return classify(edit, Category.NOUN_POSS);
            }

        } else if (edit.matches(ofSize(2, 1))) {

            String sourceLemma = edit.source().first().lemma();
            String targetLemma = edit.target().first().lemma();

            String possessiveCase = edit.source().first().next().map(Token::tag).orElse("");
            if (sourceLemma.equals(targetLemma) && isPossessiveCase(possessiveCase)) {
                return classify(edit, Category.NOUN_POSS);
            }
        }
        return unknown(edit);
    }

    private boolean isPossessiveCase(String tag) {
        return PtbPos.POS.matches(tag);
    }

}

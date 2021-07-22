package com.github.manzurola.errant4j.lang.en.classify.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.GrammaticalError;
import com.github.manzurola.errant4j.core.classify.Classifier;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.spacy4j.api.containers.Token;
import com.github.manzurola.spacy4j.api.features.Tag;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Noun possessive errors typically involve edits that change a possessive suffix on a noun phrase; e.g. [(Tom) ε →
 * (Tom) ’s] or [(Chris) ’s → (Chris) ’]. They are captured by the following rule: 1. There is exactly one token on both
 * sides of the edit, and 2. At least one side of the edit is POS tagged as a possessive suffix (POS)
 * <p>
 * While the above rule handles possessive suffixes that have become separated from their dependent nouns as a result of
 * an automatic alignment, the following rule handles multi-token edits where this is not the case; e.g. [friends →
 * friend ’s]: 1. There are exactly two tokens on at least one side of the edit, and 2. (a) The original tokens are POS
 * tagged sequentially as NOUN and PART, or (b) The corrected tokens are POS tagged sequentially as NOUN and PART, and
 * 3. The first token on both sides of the edit has the same lemma.
 */
public class NounPossessiveRule extends Classifier.Predicate {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.NOUN_POSS;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        if (edit.matches(Predicates.ofSize(1, 0).or(Predicates.ofSize(0, 1)))) {
            List<String> posSet = edit.stream().map(Token::tag).distinct().collect(toList());
            if (posSet.size() == 1 && isPossessiveCase(posSet.get(0))) {
                return true;
            }
        }

        if (edit.matches(Predicates.ofSize(1, 2))) {

            String sourceLemma = edit.source().first().lemma();
            String targetLemma = edit.target().first().lemma();

            String possessiveCase = edit.target().first().next().map(Token::tag).orElse("");
            return sourceLemma.equals(targetLemma) && isPossessiveCase(possessiveCase);

        } else if (edit.matches(Predicates.ofSize(2, 1))) {

            String sourceLemma = edit.source().first().lemma();
            String targetLemma = edit.target().first().lemma();

            String possessiveCase = edit.source().first().next().map(Token::tag).orElse("");
            return sourceLemma.equals(targetLemma) && isPossessiveCase(possessiveCase);
        }

        return false;
    }

    private boolean isPossessiveCase(String tag) {
        return Tag.POS.matches(tag);
    }

}

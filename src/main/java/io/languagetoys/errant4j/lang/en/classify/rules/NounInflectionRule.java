package io.languagetoys.errant4j.lang.en.classify.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.errant4j.core.annotate.Classifier;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.errant4j.lang.en.utils.wordlist.WordList;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.function.Predicate;

/**
 * Noun inflection errors are usually count-mass noun errors, e.g. [advices → advice], but also include cases such as
 * [countrys → countries] and [childs → children]. They are a special kind of non-word error that must meet the
 * following criteria: 1. There is exactly one token on both sides of the edit, and 2. The original token is entirely
 * alphabetical (i.e. no numbers or punctuation), and 3. The original token is not in the Hunspell word list, and 4. The
 * lower cased form of the original token is also not in the Hunspell word list, and 5. The original and corrected
 * tokens have the same lemma, and 6. The original and corrected tokens are both POS tagged as NOUN.
 */
public class NounInflectionRule extends Classifier.Predicate {

    private final WordList wordList;

    public NounInflectionRule(WordList wordList) {
        this.wordList = wordList;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.NOUN_INFL;
    }

    @Override
    public boolean test(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(e -> e.source().allMatch(Token::isAlpha))
                .filter(e -> e.source().allMatch(isNotRealWord()))
                .filter(sameLemma())
                .filter(e -> e.stream().allMatch(Predicates.isNoun()))
                .isPresent();
    }

    public Predicate<Token> isNotRealWord() {
        return token -> !wordList.contains(token.text()) && !wordList.contains(token.lower());
    }

    public Predicate<Edit<Token>> sameLemma() {
        return edit -> edit.source().first().lemma().equals(edit.target().first().lemma());
    }
}

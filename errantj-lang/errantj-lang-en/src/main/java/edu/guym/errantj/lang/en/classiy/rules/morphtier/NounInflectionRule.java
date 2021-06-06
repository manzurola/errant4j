package edu.guym.errantj.lang.en.classiy.rules.morphtier;

import edu.guym.errantj.core.classify.Category;
import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.rules.Rule;
import edu.guym.errantj.lang.en.classiy.common.TokenPredicates;
import edu.guym.errantj.wordlist.WordList;
import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.predicates.EditPredicates;

import java.util.function.Predicate;

/**
 * Noun inflection errors are usually count-mass noun errors, e.g. [advices → advice], but also
 * include cases such as [countrys → countries] and [childs → children].
 * They are a special kind of non-word error that must meet the following criteria:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. The original token is entirely alphabetical (i.e. no numbers or punctuation), and
 * 3. The original token is not in the Hunspell word list, and
 * 4. The lower cased form of the original token is also not in the Hunspell word list, and
 * 5. The original and corrected tokens have the same lemma, and
 * 6. The original and corrected tokens are both POS tagged as NOUN.
 */
public class NounInflectionRule implements Rule {

    private final WordList wordList;

    public NounInflectionRule(WordList wordList) {
        this.wordList = wordList;
    }

    @Override
    public GrammaticalError apply(Edit<Token> edit) {
        return edit
                .filter(EditPredicates.ofSizeOneToOne())
                .filter(e -> e.source().allMatch(Token::isAlpha))
                .filter(e -> e.source().allMatch(isNotRealWord()))
                .filter(sameLemma())
                .filter(e -> e.stream().allMatch(TokenPredicates.isNoun()))
                .map(classify(Category.NOUN_INFL))
                .orElse(unknown(edit));
    }

    public Predicate<Token> isNotRealWord() {
        return token -> !wordList.contains(token.text()) && !wordList.contains(token.lowerCase());
    }

    public Predicate<Edit<Token>> sameLemma() {
        return edit -> edit.source().first().lemma().equals(edit.target().first().lemma());
    }
}

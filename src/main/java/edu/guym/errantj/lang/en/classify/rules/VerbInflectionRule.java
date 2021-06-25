package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.errantj.lang.en.classify.CategoryMatchRule;
import edu.guym.errantj.lang.en.utils.wordlist.WordList;
import edu.guym.spacyj.api.containers.Token;

import java.util.function.Predicate;

/**
 * Verb inflection errors are classified in a similar manner to noun inflection errors,
 * and are a special kind of non-word error.
 * Examples include: [getted → got],[danceing → dancing] and [fliped → flipped].
 * 1. There is exactly one token on both sides of the edit, and
 * 2. The original token is entirely alphabetical (i.e. no numbers or punctuation), and
 * 3. The original token is not in the Hunspell word list, and
 * 4. The lower cased form of the original token is also not in the Hunspell word list, and
 * 5. The original and corrected tokens have the same lemma, and
 * 6. The original and corrected tokens are both POS tagged as VERB.
 */
public class VerbInflectionRule extends CategoryMatchRule {

    private final WordList wordList;

    public VerbInflectionRule(WordList wordList) {
        this.wordList = wordList;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.VERB_INFL;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSizeOneToOne())
                .filter(e -> e.source().allMatch(Token::isAlpha))
                .filter(e -> e.source().allMatch(isNotRealWord()))
                .filter(sameLemma())
                .filter(e -> e.stream().allMatch(Predicates.isVerb()))
                .isPresent();
    }

    public Predicate<Token> isNotRealWord() {
        return token -> !wordList.contains(token.text()) && !wordList.contains(token.lower());
    }


    public Predicate<Edit<Token>> sameLemma() {
        return edit -> edit.source().first().lemma().equals(edit.target().first().lemma());
    }
}

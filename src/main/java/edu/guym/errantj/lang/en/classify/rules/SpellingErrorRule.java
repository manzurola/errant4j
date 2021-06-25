package edu.guym.errantj.lang.en.classify.rules;

import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.utils.AlignerUtils;
import edu.guym.errantj.core.errors.GrammaticalError;
import edu.guym.errantj.lang.en.classify.CategoryMatchRule;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.errantj.lang.en.utils.wordlist.WordList;
import edu.guym.spacyj.api.containers.Token;

/**
 * We use the latest British English Hunspell word list to identify spelling errors (see Section 5.2).
 * It is straightforward to replace this word list with one for other varieties of English if needed.
 * We assume the corrected side of an edit is always a valid word. Spelling errors must meet the following conditions:
 * 1. There is exactly one token on both sides of the edit, and
 * 2. The original token is entirely alphabetical (i.e. no numbers or punctuation), and
 * 3. The original token is not in the Hunspell word list, and
 * 4. The lower cased form of the original token is also not in the Hunspell word list, and
 * 5. The original and corrected tokens do not have the same lemma, and
 * 6. The original and corrected tokens share at least 50% of the same characters in the same relative order.
 */
public class SpellingErrorRule extends CategoryMatchRule {

    private final WordList wordList;

    public SpellingErrorRule(WordList wordList) {
        this.wordList = wordList;
    }

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.SPELL;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        return edit
                .filter(Predicates.ofSize(1, 1))
                .filter(e -> e.source().first().isAlpha())
                .filter(e -> !wordList.contains(e.source().first().text()))
                .filter(e -> !wordList.contains(e.source().first().lower()))
                .filter(e -> !e.source().first().lemma().equals(e.target().first().lemma()))
                .filter(e -> AlignerUtils.charEditRatio(e.source().first().text(), e.target().first().text()) > 0.5)
                .isPresent();
    }
}

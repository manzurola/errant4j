package edu.guym.errantj.lang.en.classiy.rules.postier;

import edu.guym.errantj.core.classify.GrammaticalError;
import edu.guym.errantj.core.classify.CategoryMatchRule;
import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.predicates.EditPredicates;

import java.util.List;
import java.util.stream.Collectors;

import static edu.guym.errantj.lang.en.classiy.common.TokenPredicates.isPunctuation;

/**
 * The following special PUNCT rule captures edits where a change in punctuation also affects the case of
 * the following word; e.g. [. Because → , because] and [Because → , because].
 */
public class PunctuationEffectRule extends CategoryMatchRule {

    @Override
    public GrammaticalError.Category getCategory() {
        return GrammaticalError.Category.PUNCT;
    }

    @Override
    public boolean isSatisfied(Edit<Token> edit) {
        // 1. The lower cased form of the last token is the same on both sides, and
        // 2. All remaining tokens are punctuation.

        if (edit.matches(EditPredicates.isSubstitute())) {
            Token sourceLast = edit.source().last();
            Token targetLast = edit.target().last();
            List<Token> remaining = edit.stream()
                    .filter(word -> !word.equals(sourceLast) && !word.equals(targetLast))
                    .collect(Collectors.toList());

            return sourceLast.lowerCase().equals(targetLast.lowerCase()) &&
                    remaining.stream().allMatch(isPunctuation());
        }

        return false;
    }
}

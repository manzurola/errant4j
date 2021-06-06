package edu.guym.errantj.lang.en.merge.conditions;

import io.squarebunny.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

import java.util.List;

public class WhiteSpaceDifferenceMergeCondition implements EditMergeCondition {

    // TODO - this can only handle max 2 words
    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        List<String> sourceTokens = left.source().map(Token::text).tokens();
        List<String> targetTokens = left.target().map(Token::text).tokens();
        sourceTokens.addAll(right.source().map(Token::text).tokens());
        targetTokens.addAll(right.target().map(Token::text).tokens());

        StringBuilder sourceConcat = new StringBuilder();
        StringBuilder targetConcat = new StringBuilder();
        sourceTokens.forEach(sourceConcat::append);
        targetTokens.forEach(targetConcat::append);

        return sourceConcat.toString().equals(targetConcat.toString());
    }
}

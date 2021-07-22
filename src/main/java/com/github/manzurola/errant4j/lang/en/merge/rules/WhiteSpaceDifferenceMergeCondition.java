package com.github.manzurola.errant4j.lang.en.merge.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.merge.Merger;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;

public class WhiteSpaceDifferenceMergeCondition implements Merger.Rule {

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

package io.github.manzurola.errant4j.lang.en.merge;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.merge.MergePredicate;
import io.github.manzurola.errant4j.lang.en.merge.rules.*;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;

public class EnMergeRules implements MergePredicate<Token> {

    private final List<MergePredicate<Token>> rules;

    public EnMergeRules() {
        this.rules = List.of(
                new PunctuationAndCaseChangeMergeCondition(),
                new PossessiveSuffixMergeCondition(),
                new WhiteSpaceDifferenceMergeCondition(),
                new SamePosMergeCondition(),
                new ContentWordMergeCondition()
        );
    }

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return rules.stream().anyMatch(rule -> rule.test(left, right));
    }
}

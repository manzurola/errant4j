package io.languagetoys.errant4j.lang.en.merge.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.annotator.MergeRule;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.spacy4j.api.containers.Token;

/**
 * Merge any consecutive operations that involve at least one content word;
 * e.g. [On → In] + [the → ε] + [other → ε] + [hand → addition] = [On the other hand → In addition].
 */
public class ContentWordMergeCondition implements MergeRule {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return left.stream().allMatch(Predicates.isContentWord()) &&
                right.stream().allMatch(Predicates.isContentWord());
    }

}

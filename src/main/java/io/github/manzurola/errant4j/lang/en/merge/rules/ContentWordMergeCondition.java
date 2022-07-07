package io.github.manzurola.errant4j.lang.en.merge.rules;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.merge.Merger;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;

/**
 * Merge any consecutive operations that involve at least one content word; e.g. [On → In] + [the → ε] + [other → ε] +
 * [hand → addition] = [On the other hand → In addition].
 */
public class ContentWordMergeCondition implements Merger.Rule {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return left.stream().allMatch(Predicates.isContentWord()) &&
               right.stream().allMatch(Predicates.isContentWord());
    }

}

package io.github.manzurola.errant4j.lang.en.merge.rules;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.merge.MergePredicate;
import io.github.manzurola.errant4j.core.merge.Merger;
import io.github.manzurola.spacy4j.api.containers.Token;

public class PossessiveSuffixMergeCondition implements MergePredicate<Token> {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return !right.target().isEmpty() && right.target().first().text().equals("'s") ||
               !right.source().isEmpty() && right.source().first().text().equals("'s");
    }

}

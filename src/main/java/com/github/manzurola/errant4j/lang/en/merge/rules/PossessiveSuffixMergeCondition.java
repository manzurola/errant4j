package com.github.manzurola.errant4j.lang.en.merge.rules;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.errant4j.core.merge.Merger;
import com.github.manzurola.spacy4j.api.containers.Token;

public class PossessiveSuffixMergeCondition implements Merger.Rule {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return !right.target().isEmpty() && right.target().first().text().equals("'s") ||
               !right.source().isEmpty() && right.source().first().text().equals("'s");
    }

}

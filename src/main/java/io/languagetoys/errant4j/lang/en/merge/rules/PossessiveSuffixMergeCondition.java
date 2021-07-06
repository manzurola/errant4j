package io.languagetoys.errant4j.lang.en.merge.rules;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.containers.Token;

public class PossessiveSuffixMergeCondition implements Merger.Rule {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return !right.target().isEmpty() && right.target().first().text().equals("'s") ||
                !right.source().isEmpty() && right.source().first().text().equals("'s");
    }

}
package edu.guym.errantj.lang.en.merge.conditions;

import edu.guym.aligner.edit.Edit;
import edu.guym.spacyj.api.containers.Token;

public class PossessiveSuffixMergeCondition implements EditMergeCondition {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return !right.target().isEmpty() && right.target().first().text().equals("'s") ||
                !right.source().isEmpty() && right.source().first().text().equals("'s");
    }

}

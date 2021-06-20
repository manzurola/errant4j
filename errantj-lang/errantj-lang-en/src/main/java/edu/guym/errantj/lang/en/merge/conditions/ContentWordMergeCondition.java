package edu.guym.errantj.lang.en.merge.conditions;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.lang.en.classify.rules.common.Predicates;
import edu.guym.spacyj.api.containers.Token;

/**
 * Merge any consecutive operations that involve at least one content word;
 * e.g. [On → In] + [the → ε] + [other → ε] + [hand → addition] = [On the other hand → In addition].
 */
public class ContentWordMergeCondition implements EditMergeCondition {

    @Override
    public boolean test(Edit<Token> left, Edit<Token> right) {
        return left.stream().allMatch(Predicates.isContentWord()) &&
                right.stream().allMatch(Predicates.isContentWord());
    }

}

package io.languagetoys.errant4j.core.annotator;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

public interface Merger {

    /**
     * Merge a list of Edits, producing a new list of equal or smaller size.
     */
    List<Edit<Token>> merge(List<Edit<Token>> edits);

    static Merger allSplit() {
        return new RuleBasedMerger((a, b) -> false);
    }

    static Merger allMerge() {
        return new RuleBasedMerger((a, b) -> true);
    }

    static Merger allEqual() {
        return new RuleBasedMerger((a, b) -> a.operation().equals(b.operation()));
    }

    static Merger rules(List<MergeRule> rules) {
        return new RuleBasedMerger((a, b) -> rules.stream().anyMatch(rule -> rule.test(a, b)));
    }
}

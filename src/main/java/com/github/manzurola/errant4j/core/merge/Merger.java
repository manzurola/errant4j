package com.github.manzurola.errant4j.core.merge;

import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.function.BiPredicate;

public interface Merger {

    /**
     * Merge a list of Edits, producing a new list of equal or smaller size.
     */
    List<Edit<Token>> merge(List<Edit<Token>> edits);

    /**
     * Get a new merger that doesn't merge any edit.
     */
    static Merger allSplit() {
        return new MergerImpl((a, b) -> false);
    }

    /**
     * Get a new merger that merges all edits.
     */
    static Merger allMerge() {
        return new MergerImpl((a, b) -> true);
    }

    /**
     * Get a new merger that merges edits of equal operation.
     */
    static Merger allEqual() {
        return new MergerImpl((a, b) -> a.operation().equals(b.operation()));
    }

    /**
     * Get a new rule based merger.
     */
    static Merger rules(List<Rule> rules) {
        return new MergerImpl((a, b) -> rules.stream().anyMatch(rule -> rule.test(a, b)));
    }

    interface Rule extends BiPredicate<Edit<Token>, Edit<Token>> {
        boolean test(Edit<Token> left, Edit<Token> right);
    }
}

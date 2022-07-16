package io.github.manzurola.errant4j.core.merge;

import io.github.manzurola.aligner.edit.Edit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class Merger<T> {

    private final MergeFilter<T> filter;
    private final MergePredicate<T> predicate;
    private final MergeAction<T> action;

    private Merger(MergeFilter<T> filter, MergePredicate<T> predicate, MergeAction<T> action) {
        this.filter = filter;
        this.predicate = predicate;
        this.action = action;
    }

    public static <T> Merger<T> create(MergeFilter<T> filter, MergePredicate<T> predicate, MergeAction<T> action) {
        return new Merger<>(filter, predicate, action);
    }

    /**
     * Merge a list of Edits, producing a new list of equal or smaller size.
     */
    public List<Edit<T>> merge(List<Edit<T>> edits) {
        if (edits.isEmpty() || edits.size() == 1) {
            return edits;
        }

        LinkedList<Edit<T>> sorted = edits.stream().sorted().collect(Collectors.toCollection(LinkedList::new));
        List<Edit<T>> merged = new ArrayList<>();
        Edit<T> current = sorted.pop();

        while (!sorted.isEmpty()) {
            Edit<T> next = sorted.pop();
            if (filter.filter(current) &&
                    filter.filter(next) &&
                    current.isLeftSiblingOf(next) &&
                    predicate.test(current, next)) {
                current = action.merge(current, next);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }


//    /**
//     * Get a new merger that doesn't merge any edit.
//     */
//    static Merger allSplit() {
//        return new MergerImpl((a, b) -> false);
//    }
//
//    /**
//     * Get a new merger that merges all edits.
//     */
//    static Merger allMerge() {
//        return new MergerImpl((a, b) -> true);
//    }
//
//    /**
//     * Get a new merger that merges edits of equal operation.
//     */
//    static Merger allEqual() {
//        return new MergerImpl((a, b) -> a.operation().equals(b.operation()));
//    }
//
//    /**
//     * Get a new rule based merger.
//     */
//    static Merger rules(List<Rule> rules) {
//        return new MergerImpl((a, b) -> rules.stream().anyMatch(rule -> rule.test(a, b)));
//    }

}

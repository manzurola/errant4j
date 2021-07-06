package io.languagetoys.errant4j.core.annotate;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.aligner.edit.EqualEdit;
import io.languagetoys.aligner.edit.TransposeEdit;
import io.languagetoys.errant4j.lang.en.merge.rules.*;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

final class MergerImpl implements Merger {

    private final BiPredicate<Edit<Token>, Edit<Token>> strategy;

    MergerImpl(BiPredicate<Edit<Token>, Edit<Token>> strategy) {
        this.strategy = strategy;
    }

    @Override
    public final List<Edit<Token>> merge(List<Edit<Token>> edits) {
        List<Edit<Token>> unmergeable = new ArrayList<>();
        List<Edit<Token>> result = new ArrayList<>(edits);
        boolean moreToMerge = true;
        while (moreToMerge) {
            moreToMerge = false;
            unmergeable.addAll(filterUnmergeable(result));
            result.removeAll(unmergeable);

            List<Edit<Token>> applied = applyConditions(result);
            if (!equalsUnique(result, applied)) {
                result = applied;
                moreToMerge = true;
            }
        }
        result.addAll(unmergeable);
        return sort(result);
    }

    private boolean equalsUnique(List<Edit<Token>> a, List<Edit<Token>> b) {
        return new HashSet<>(a).equals(new HashSet<>(b));
    }

    private List<Edit<Token>> filterUnmergeable(List<Edit<Token>> edits) {
        return edits.stream()
                .filter(e -> e instanceof TransposeEdit || e instanceof EqualEdit)
                .collect(Collectors.toList());
    }

    public final List<Edit<Token>> applyConditions(List<Edit<Token>> edits) {

        if (edits.isEmpty() || edits.size() == 1) {
            return edits;
        }

        LinkedList<Edit<Token>> sorted = sort(edits);
        List<Edit<Token>> merged = new ArrayList<>();
        Edit<Token> current = sorted.pop();

        while (!sorted.isEmpty()) {
            Edit<Token> next = sorted.pop();
            if (current.isLeftSiblingOf(next) && strategy.test(current, next)) {
                current = current.mergeWith(next);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }

    private BiPredicate<Edit<Token>, Edit<Token>> getConditions() {
        return new PunctuationAndCaseChangeMergeCondition()
                .or(new PossessiveSuffixMergeCondition())
                .or(new WhiteSpaceDifferenceMergeCondition())
                .or(new SamePosMergeCondition())
                .or(new ContentWordMergeCondition());
    }

    private LinkedList<Edit<Token>> sort(List<Edit<Token>> edits) {
        LinkedList<Edit<Token>> sorted = new LinkedList<>(edits);
        Collections.sort(sorted);
        return sorted;
    }
}

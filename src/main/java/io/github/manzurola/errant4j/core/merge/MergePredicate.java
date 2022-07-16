package io.github.manzurola.errant4j.core.merge;

import io.github.manzurola.aligner.edit.Edit;

import java.util.function.BiPredicate;

public interface MergePredicate<T> extends BiPredicate<Edit<T>, Edit<T>> {
    boolean test(Edit<T> left, Edit<T> right);
}

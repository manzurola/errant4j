package io.github.manzurola.errant4j.core.merge;

import io.github.manzurola.aligner.edit.Edit;

public interface MergeAction<T> {

    Edit<T> merge(Edit<T> left, Edit<T> right);
}

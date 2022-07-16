package io.github.manzurola.errant4j.core.merge;

import io.github.manzurola.aligner.edit.Edit;

public interface MergeFilter<T> {
    boolean filter(Edit<T> edit);
}

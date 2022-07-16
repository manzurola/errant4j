package io.github.manzurola.errant4j.core.merge;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.aligner.edit.Operation;

public class ExcludeTransposeAndEqualsMergeFilter<T> implements MergeFilter<T> {
    @Override
    public boolean filter(Edit<T> edit) {
        return !edit.operation().equals(Operation.TRANSPOSE) &&
                !edit.operation().equals(Operation.EQUAL);
    }
}

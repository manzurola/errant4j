package io.github.manzurola.errant4j.core.merge;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.aligner.edit.Operation;
import io.github.manzurola.aligner.edit.Segment;

public class SameOpOrSubstituteMergeAction<T> implements MergeAction<T> {
    @Override
    public Edit<T> merge(Edit<T> left, Edit<T> right) {

        Segment<T> source = left.source().append(right.source().tokens());
        Segment<T> target = left.target().append(right.target().tokens());
        Operation operation = left.operation().equals(right.operation()) ? left.operation() : Operation.SUBSTITUTE;

        return Edit.of(source, target, operation);
    }
}

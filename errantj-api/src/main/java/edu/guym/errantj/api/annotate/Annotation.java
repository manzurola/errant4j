package edu.guym.errantj.api.annotate;

import edu.guym.aligner.edit.Edit;
import edu.guym.errantj.api.errors.GrammaticalError;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Annotation<T> {

    private final Edit<T> edit;
    private final GrammaticalError error;

    private Annotation(Edit<T> edit) {
        this(edit, GrammaticalError.NONE);
    }

    public Annotation(Edit<T> edit, GrammaticalError error) {
        this.edit = edit;
        this.error = error;
    }

    public static <T> Annotation<T> create(Edit<T> edit, GrammaticalError error) {
        return new Annotation<>(edit, error);
    }

    public static <T> Annotation<T> of(Edit<T> edit) {
        return new Annotation<T>(edit);
    }

    public final Edit<T> edit() {
        return edit;
    }

    public final boolean matches(Predicate<? super Annotation<T>> predicate) {
        return predicate.test(this);
    }

    public final <E> Annotation<E> map(Function<? super Edit<T>, ? extends Edit<E>> teFunction) {
        return new Annotation<>(teFunction.apply(edit), error);
    }

    public final <R> R transform(Function<? super Annotation<T>, ? extends R> mapper) {
        return mapper.apply(this);
    }

    public final Annotation<T> withError(GrammaticalError error) {
        return new Annotation<>(edit(), error);
    }

    public final GrammaticalError getError() {
        return error;
    }

    public final boolean hasError() {
        return !getError().isNone() && !getError().isIgnored();
    }

    public final boolean hasNoError() {
        return !hasError();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Annotation<?> that = (Annotation<?>) o;
        return error == that.error;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public final String toString() {
        return "Annotation{" +
                "edit=" + edit +
                "error=" + error +
                "} ";
    }


}

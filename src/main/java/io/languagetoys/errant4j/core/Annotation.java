package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.tools.mark.CharOffset;
import io.languagetoys.errant4j.core.tools.mark.ErrorMarker;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;
import io.languagetoys.spacy4j.api.utils.TextUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An Annotation matches an {@link Edit} with its associated {@link GrammaticalError}.
 */
public final class Annotation {

    private final Edit<Token> edit;
    private final GrammaticalError error;

    private Annotation(Edit<Token> edit) {
        this(edit, GrammaticalError.NONE);
    }

    public Annotation(Edit<Token> edit, GrammaticalError error) {
        this.edit = edit;
        this.error = error;
    }

    public static Annotation of(Edit<Token> edit, GrammaticalError error) {
        return new Annotation(edit, error);
    }

    public static Annotation of(Edit<Token> edit) {
        return new Annotation(edit);
    }

    public final Edit<Token> edit() {
        return edit;
    }

    public final String sourceText() {
        return TextUtils.writeTextWithoutWs(edit
                                                    .source()
                                                    .tokens()
                                                    .stream()
                                                    .map(Token::data)
                                                    .collect(Collectors.toList()));
    }

    public final String targetText() {
        return TextUtils.writeTextWithoutWs(edit
                                                    .target()
                                                    .tokens()
                                                    .stream()
                                                    .map(Token::data)
                                                    .collect(Collectors.toList()));
    }

    public final boolean matches(Predicate<? super Annotation> predicate) {
        return predicate.test(this);
    }

    public final <R> R transform(Function<? super Annotation, ? extends R> mapper) {
        return mapper.apply(this);
    }

    public final GrammaticalError grammaticalError() {
        return error;
    }

    public final Annotation setGrammaticalError(GrammaticalError error) {
        return new Annotation(edit(), error);
    }

    public final boolean isError() {
        return !grammaticalError().isNone() && !grammaticalError().isIgnored();
    }

    public final boolean isNotError() {
        return !isError();
    }

    public final CharOffset markErrorInSource() {
        return edit.accept(new ErrorMarker(edit.source().tokens()));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Annotation that = (Annotation) o;
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

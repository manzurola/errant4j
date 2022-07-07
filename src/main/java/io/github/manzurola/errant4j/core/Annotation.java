package io.github.manzurola.errant4j.core;

import com.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;

/**
 * An Annotation matches an {@link Edit} with its associated {@link
 * GrammaticalError}.
 */
public final class Annotation {

    private final Edit<Token> edit;
    private final GrammaticalError error;

    private Annotation(Edit<Token> edit, GrammaticalError error) {
        this.edit = Objects.requireNonNull(edit);
        this.error = Objects.requireNonNull(error);
    }

    public static Annotation of(Edit<Token> edit, GrammaticalError error) {
        return new Annotation(edit, error);
    }

    public final Edit<Token> edit() {
        return edit;
    }

    public final GrammaticalError error() {
        return error;
    }

    public final int sourcePosition() {
        return edit.source().position();
    }

    public final List<Token> sourceTokens() {
        return edit.source().tokens();
    }

    public final String sourceText() {
        return concatTokenText(edit.source().tokens());
    }

    private String concatTokenText(List<Token> tokens) {
        return tokens
            .stream()
            .map(Token::textWithWs)
            .reduce("", String::concat)
            .trim();
    }

    public final int targetPosition() {
        return edit.target().position();
    }

    public final List<Token> targetTokens() {
        return edit.target().tokens();
    }

    public final String targetText() {
        return concatTokenText(edit.target().tokens());
    }

    @Deprecated
    public final boolean hasError() {
        return !GrammaticalError.NONE.equals(error());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Annotation that = (Annotation) o;
        return error == that.error;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public final String toString() {
        return error + ", " + edit;
    }

}

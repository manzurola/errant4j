package io.github.manzurola.errant4j.core;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.errant4j.core.errors.GrammaticalError;
import io.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;

/**
 * An Annotation matches an {@link Edit} with its associated {@link GrammaticalError}.
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

    public Edit<Token> edit() {
        return edit;
    }

    public GrammaticalError error() {
        return error;
    }

    public int sourcePosition() {
        return edit.source().position();
    }

    public List<Token> sourceTokens() {
        return edit.source().tokens();
    }

    public String sourceText() {
        return concatTokenText(edit.source().tokens());
    }

    private String concatTokenText(List<Token> tokens) {
        return tokens
                .stream()
                .map(Token::textWithWs)
                .reduce("", String::concat)
                .trim();
    }

    public int targetPosition() {
        return edit.target().position();
    }

    public List<Token> targetTokens() {
        return edit.target().tokens();
    }

    public String targetText() {
        return concatTokenText(edit.target().tokens());
    }

    @Deprecated
    public boolean hasError() {
        return !GrammaticalError.NONE.equals(error());
    }

    @Override
    public boolean equals(Object o) {
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
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return error + ", " + edit;
    }

}

package io.languagetoys.errant4j.core.tools;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.aligner.edit.Segment;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.LinkedList;
import java.util.List;

public final class TokenEditUtils {

    private TokenEditUtils() {
    }

    public static String getSourceText(Edit<Token> edit) {
        return concatenateWordsWithSpaces(edit.source());
    }

    public static String getTargetText(Edit<Token> edit) {
        return concatenateWordsWithSpaces(edit.target());
    }

    public static String getWhitespaceBefore(Segment<Token> segment) {
        if (segment.isEmpty()) {
            return "";
        }
        return segment.first().spaceBefore();
    }

    public static String getWhitespaceAfter(Segment<Token> segment) {
        if (segment.isEmpty()) {
            return "";
        }
        return segment.last().spaceAfter();
    }

    public static String concatenateWordsWithSpaces(Segment<Token> segment) {
        return concatenateWordsWithSpaces(segment.tokens());
    }

    public static String concatenateWordsWithSpaces(List<Token> segment) {
        LinkedList<Token> tokens = new LinkedList<>(segment);
        if (tokens.isEmpty()) {
            return "";
        }
        if (tokens.size() == 1) {
            return tokens.getFirst().text();
        }

        StringBuilder text = new StringBuilder();
        String after = "";
        for (Token token : tokens) {
            text.append(token.text());
            after = token.spaceAfter();
            if (token.index() != tokens.getLast().index()) {
                text.append(after);
            }
        }
        return text.toString();
    }

    public static Edit<Token> toTokenEdit(Edit<String> edit, final List<Token> source, final List<Token> target) {
        return edit.mapSegments(
                s -> s.mapWithIndex(source::get),
                t -> t.mapWithIndex(target::get)
        );
    }

    public static Edit<Token> toTokenEdit(Edit<String> edit, final Doc source, final Doc target) {
        return toTokenEdit(edit, source.tokens(), target.tokens());
    }

}

package com.github.manzurola.errant4j.core.mark;

import com.github.manzurola.errant4j.core.Annotation;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WideInsertErrorMarker implements ErrorMarker {
    @Override
    public MarkedError markError(Annotation annotation, List<Token> source) {
        if (annotation.sourceTokens().isEmpty()) {
            return markSourceNeighbors(annotation, source);
        }
        return markSource(annotation, source);
    }

    private MarkedError markSource(Annotation annotation, List<Token> source) {
        int firstTokenPos = annotation.sourcePosition();
        int lastTokenPos = annotation.sourcePosition() +
                           annotation.sourceTokens().size();
        List<Token> sourceTokens = source.subList(firstTokenPos, lastTokenPos);
        int start = sourceTokens.get(0).charStart();
        int end = sourceTokens.get(sourceTokens.size() - 1).charEnd();
        String original = Token.getTextRaw(sourceTokens).trim();
        String replacement = annotation.targetText();
        return new MarkedError(start, end, original, replacement);
    }

    private MarkedError markSourceNeighbors(
        Annotation annotation,
        List<Token> source
    ) {
        // source in edit is empty
        int beforeIndex = annotation.sourcePosition() - 1;
        int afterIndex = annotation.sourcePosition();

        Optional<Token> before = getOptionalToken(beforeIndex, source);
        Optional<Token> after = getOptionalToken(afterIndex, source);

        // if has before and after tokens, mark both
        // else if has before, get before
        // else if has whitespaceAfter, get whitespaceAfter
        // else - empty source sentence, return 0,0

        List<Token> originalTokens = new ArrayList<>();
        List<Token> replacementTokens =
            new ArrayList<>(annotation.targetTokens());

        int charStart = 0;
        int charEnd = 0;

        if (before.isPresent() && after.isPresent()) {
            charStart = before.get().charStart();
            charEnd = after.get().charEnd();
            replacementTokens.add(0, before.get());
            replacementTokens.add(after.get());
            originalTokens.addAll(List.of(before.get(), after.get()));
        } else if (before.isPresent()) {
            charStart = before.get().charStart();
            charEnd = before.get().charEnd();
            replacementTokens.add(0, before.get());
            originalTokens.add(before.get());
        } else if (after.isPresent()) {
            charStart = after.get().charStart();
            charEnd = after.get().charEnd();
            replacementTokens.add(after.get());
            originalTokens.add(after.get());
        }

        String original = Token.getTextRaw(originalTokens).trim();
        String replacement = Token.getTextRaw(replacementTokens).trim();

        return new MarkedError(charStart, charEnd, original, replacement);
    }

    private Optional<Token> getOptionalToken(int index, List<Token> tokens) {
        try {
            Objects.checkIndex(index, tokens.size());
            return Optional.of(tokens.get(index));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}

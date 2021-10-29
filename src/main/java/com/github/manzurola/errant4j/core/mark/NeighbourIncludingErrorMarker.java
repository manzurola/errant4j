package com.github.manzurola.errant4j.core.mark;

import com.github.manzurola.errant4j.core.Annotation;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NeighbourIncludingErrorMarker implements ErrorMarker {
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

        Optional<Token> before = getOptionalToken(annotation.sourcePosition() -
                                                  1, source);
        Optional<Token> after = getOptionalToken(
            annotation.sourcePosition(),
            source
        );

        List<Token> originalTokens = Stream
            .of(before, after)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        List<Token> replacementTokens = Stream.concat(
                Stream
                    .of(before)
                    .filter(Optional::isPresent)
                    .map(Optional::get),
                Stream
                    .concat(
                        annotation.targetTokens().stream(),
                        Stream
                            .of(after)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                    )
            )
            .collect(Collectors.toList());

        String original = Token.getTextRaw(originalTokens).trim();
        String replacement = Token.getTextRaw(replacementTokens).trim();

        int charStart = originalTokens.isEmpty() ?
                        0 :
                        originalTokens.get(0).charStart();

        int charEnd = originalTokens.isEmpty() ?
                      0 :
                      originalTokens.get(originalTokens.size() - 1).charEnd();

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

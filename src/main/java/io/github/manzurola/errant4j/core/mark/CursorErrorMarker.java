package io.github.manzurola.errant4j.core.mark;

import io.github.manzurola.errant4j.core.Annotation;
import io.github.manzurola.spacy4j.api.containers.Token;
import io.github.manzurola.spacy4j.api.utils.TextUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CursorErrorMarker implements ErrorMarker {

    @Override
    public MarkedError markError(Annotation annotation, List<Token> source) {
        int sourceTokenStart = annotation.sourcePosition();
        int sourceTokenEnd = annotation.sourcePosition() +
                             annotation.sourceTokens().size();
        Objects.checkFromToIndex(
            sourceTokenStart,
            sourceTokenEnd,
            source.size()
        );
        if (annotation.sourceTokens().isEmpty()) {
            return markInsertLocation(annotation, source);
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
        String original = TextUtils.concatTokenTextWithWs(sourceTokens).trim();
        String replacement = annotation.targetText();
        return new MarkedError(start, end, original, replacement);
    }

    private MarkedError markInsertLocation(
        Annotation annotation,
        List<Token> source
    ) {

        Optional<Token> sourceToken = getOptionalToken(
            annotation.sourcePosition(),
            source
        );

        int charOffset = sourceToken.map(Token::charStart).orElse(0);

        String original = "";
        String replacement = TextUtils.concatTokenTextWithWs(annotation.targetTokens()).trim();
        if (source.isEmpty()) {
            return new MarkedError(0, 0, "", replacement);
        }

        return new MarkedError(charOffset, charOffset, original, replacement);
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

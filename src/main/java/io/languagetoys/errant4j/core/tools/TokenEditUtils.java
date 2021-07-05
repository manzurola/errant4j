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

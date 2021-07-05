package io.languagetoys.errant4j.core.annotator;

import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

public interface Merger {

    /**
     * Merge a list of Edits, producing a new list of equal or smaller size.
     */
    List<Edit<Token>> merge(List<Edit<Token>> edits);
}

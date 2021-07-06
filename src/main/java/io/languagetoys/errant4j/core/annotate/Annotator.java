package io.languagetoys.errant4j.core.annotate;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;

public interface Annotator {

    Alignment<Token> align(List<Token> source, List<Token> target);

    List<Edit<Token>> merge(List<Edit<Token>> edits);

    GrammaticalError classify(Edit<Token> edit);

    static Annotator of(Aligner<Token> aligner, Merger merger, Classifier classifier) {
        return new AnnotatorImpl(aligner, merger, classifier);
    }
}

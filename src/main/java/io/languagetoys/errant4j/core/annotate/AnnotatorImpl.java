package io.languagetoys.errant4j.core.annotate;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.GrammaticalError;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;

final class AnnotatorImpl implements Annotator {

    private final Aligner<Token> aligner;
    private final Merger merger;
    private final Classifier classifier;

    public AnnotatorImpl(Aligner<Token> aligner,
                         Merger merger,
                         Classifier classifier) {
        this.aligner = Objects.requireNonNull(aligner);
        this.merger = Objects.requireNonNull(merger);
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public final Alignment<Token> align(List<Token> source, List<Token> target) {
        return aligner.align(source, target);
    }

    @Override
    public final List<Edit<Token>> merge(List<Edit<Token>> edits) {
        return merger.merge(edits);
    }

    @Override
    public final GrammaticalError classify(Edit<Token> edit) {
        return classifier.classify(edit);
    }
}

package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.aligner.edit.Edit;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Doc;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.List;
import java.util.Objects;

final class AnnotatorImpl implements Annotator {

    protected final SpaCy spaCy;
    protected final Aligner<Token> aligner;
    protected final Merger merger;
    protected final Classifier classifier;

    public AnnotatorImpl(SpaCy spaCy,
                          Aligner<Token> aligner,
                          Merger merger,
                          Classifier classifier) {
        this.spaCy = Objects.requireNonNull(spaCy);
        this.aligner = Objects.requireNonNull(aligner);
        this.merger = Objects.requireNonNull(merger);
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public final Doc parse(String text) {
        return spaCy.nlp(text);
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

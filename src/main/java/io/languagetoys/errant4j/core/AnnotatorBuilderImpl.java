package io.languagetoys.errant4j.core;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.spacy4j.api.SpaCy;
import io.languagetoys.spacy4j.api.containers.Token;

final class AnnotatorBuilderImpl implements Annotator.Builder {

    private SpaCy spaCy;
    private Aligner<Token> aligner;
    private Merger merger;
    private Classifier classifier;

    @Override
    public final Annotator.Builder setSpaCy(SpaCy spaCy) {
        this.spaCy = spaCy;
        return this;
    }

    @Override
    public final Annotator.Builder setAligner(Aligner<Token> aligner) {
        this.aligner = aligner;
        return this;
    }

    @Override
    public final Annotator.Builder setMerger(Merger merger) {
        this.merger = merger;
        return this;
    }

    @Override
    public final Annotator.Builder setClassifier(Classifier classifier) {
        this.classifier = classifier;
        return this;
    }

    @Override
    public final Annotator build() {
        return new AnnotatorImpl(spaCy, aligner, merger, classifier);
    }
}
